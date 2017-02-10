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
      send(new RMIException("Received null object"));
    }
    try {
      remoteCall = (RemoteCall) obj;
    } catch (ClassCastException e) {
      send(new RMIException("Received object is not an instance of RemoteCall"));
    }
    if (!validMethodNames.contains(remoteCall.getMethodName())) {
      send(new RMIException("Received method is not valid"));
    }
    try {
      Method methodOnRemoteObject = remoteObject.getClass().getMethod(remoteCall.getMethodName());
      ret = methodOnRemoteObject.invoke(remoteObject, remoteCall.getArgs());
    } catch (NoSuchMethodException e) {
      System.out.println("This should never happen");
      e.printStackTrace();
    } catch (IllegalAccessException | InvocationTargetException e) {
      send(new RMIException("Remote object failed to execute your remote call"));
    }
    send(new RemoteReturn(ret));
  }

  private void setupObjectStreams() {
    try {
      out = new ObjectOutputStream(clientSocket.getOutputStream());
      out.flush();
      in = new ObjectInputStream(clientSocket.getInputStream());
    } catch (IOException e) {
      System.out.println("Failed to setup objectStreams, exiting");
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
