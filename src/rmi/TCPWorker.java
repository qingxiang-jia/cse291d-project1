package rmi;

import java.io.*;
import java.net.Socket;

/**
 * This is the actual server that handles each client. This runs in a separate thread.
 */
public class TCPWorker implements Runnable {
  TCPServer parent;
  Socket clientSocket;
  ObjectInputStream in;
  ObjectOutputStream out;

  public TCPWorker(Socket clientSocket, TCPServer parent) {
    this.clientSocket = clientSocket;
    this.parent = parent;
  }

  public void run() {
    setupObjectStreams();
    test();
  }

  public void test() {
    String messageFromClient = (String) receive(); // blocking
    System.out.println("Received: " + messageFromClient);
    String messageFromServer = "message from server";
    send(messageFromServer);
    stopWorker();
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
      out.close();
      in.close();
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
