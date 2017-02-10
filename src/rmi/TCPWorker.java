package rmi;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

/**
 * This is the actual server that handles each client. This runs in a separate thread.
 */
public class TCPWorker<T> implements Runnable {
  TCPServer parent;
  Socket clientSocket;
  T remoteObject;
  Class<T> clazz;
  Set<String> validMethodNames;
  ObjectInputStream in;
  ObjectOutputStream out;

  public TCPWorker(Socket clientSocket, TCPServer parent) {
    this.clientSocket = clientSocket;
    this.parent = parent;
    validMethodNames = new HashSet<>();
  }

  public void run() {
    setupObjectStreams();
//    test();
    remoteObject = (T) parent.skeleton.remoteObject;
    clazz = parent.skeleton.clazz;
    for (Method method : clazz.getMethods()) {
      validMethodNames.add(method.getName());
    }
    handleRemoteInvocation();
  }

  public void test() {
    String messageFromClient = (String) receive(); // blocking
    System.out.println("Received: " + messageFromClient);
    String messageFromServer = "message from server";
    send(messageFromServer);
    stopWorker();
  }

  public void handleRemoteInvocation() {
    Object obj = receive(); // blocking
    RemoteCall remoteCall = null;
    Object ret = null;
    if (obj == null) {
      send(new RemoteReturn(new RMIException("Received null object")));
    }
    try {
      remoteCall = (RemoteCall) obj;
    } catch (ClassCastException e) {
      send(new RemoteReturn(new RMIException("Received object is not an instance of RemoteCall")));
    }
    if (!validMethodNames.contains(remoteCall.getMethodName())) {
      send(new RemoteReturn(new RMIException("Received method is not valid")));
    }
    if (remoteCall.getParaTypes() == null) {
      send(new RemoteReturn(new RMIException("Parameter list is null")));
    }
    if (remoteCall.getArgs() == null) {
      send(new RemoteReturn(new RMIException("Argument list is null")));
    }
    if (remoteCall.getParaTypes().size() != remoteCall.getArgs().size()) {
      send(new RemoteReturn(new RMIException("Method parameter quantity does not match actual argument quantity")));
    }
    Class[] parameterTypeArray = new Class[remoteCall.getParaTypes().size()];
    for (int i = 0; i < remoteCall.getParaTypes().size(); i++) {
      parameterTypeArray[i] = remoteCall.getParaTypes().get(i);
    }
    try {
      Method methodOnRemoteObject = remoteObject.getClass().getMethod(remoteCall.getMethodName(), parameterTypeArray);
      Object[] args = new Object[remoteCall.getArgs().size()];
      for (int i = 0; i < args.length; i++) {
        args[i] = remoteCall.getArgs().get(i);
      }
      ret = methodOnRemoteObject.invoke(remoteObject, args);
    } catch (NoSuchMethodException e) {
      System.out.println("This should never happen");
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      send(new RemoteReturn(new RMIException("Remote object failed to execute your remote call")));
    } catch (InvocationTargetException e) {
      RemoteReturn rRet = new RemoteReturn(ret);
      rRet.setHasException(Boolean.TRUE);
      rRet.setException(e.getTargetException());
      rRet.setExceptionType(e.getTargetException().getClass());
      send(rRet);
    }
    send(new RemoteReturn(ret));
  }

  private void setupObjectStreams() {
    try {
      out = new ObjectOutputStream(clientSocket.getOutputStream());
      out.flush();
      while (clientSocket.getInputStream().available() <= 0);
      in = new ObjectInputStream(clientSocket.getInputStream());
    } catch (IOException e) {
      System.out.println("Failed to setup objectStreams, exiting");
      e.printStackTrace();
      stopWorker();
      System.exit(-1);
    }
  }

  public void stopWorker() {
    try {
      if (out != null) {
        out.close();
      }
      if (in != null) {
        in.close();
      }
      parent.deregisterThread(Thread.currentThread());
    } catch(IOException e) {
      System.out.println("Failed to close objectStreams, exiting");
      System.exit(-1);
    }
  }

  public void send(Object obj) {
    try {
      out.writeObject(obj);
    } catch (IOException e) {
      System.out.println("Failed to send object obj");
      e.printStackTrace();
    }
  }

  public Object receive() {
    try {
      return in.readObject();
    } catch (IOException e) {
      System.out.println("Failed to read object from ObjectInputStream");
      e.printStackTrace();
      stopWorker();
    } catch (ClassNotFoundException CNFExeption) {
      System.out.println("This exception should never been thrown, something is wrong");
      CNFExeption.printStackTrace();
    }
    return null;
  }
}
