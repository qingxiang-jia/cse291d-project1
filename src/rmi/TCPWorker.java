package rmi;

import java.io.*;
import java.net.Socket;
import java.sql.SQLOutput;

/**
 * This is the actual server that handles each client. This runs in a separate thread.
 */
public class TCPWorker implements Runnable {
  Socket clientSocket;
  ObjectInputStream in;
  ObjectOutputStream out;

  public TCPWorker(Socket clientSocket) {
    this.clientSocket = clientSocket;
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
      TCPServer.root.deregisterThread(Thread.currentThread());
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