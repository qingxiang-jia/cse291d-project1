package rmi;

import java.io.*;
import java.net.Socket;

/**
 * This is the actual server that handles each client. This runs in a separate thread.
 */
public class TCPSWorker implements Runnable {
  Socket clientSocket;
  ObjectInputStream in;
  ObjectOutputStream out;

  public TCPSWorker(Socket clientSocket) {
    this.clientSocket = clientSocket;
  }

  public void run() {
    setupObjectStreams();
  }

  private void setupObjectStreams() {
    try {
      in = new ObjectInputStream(clientSocket.getInputStream());
      out = new ObjectOutputStream(clientSocket.getOutputStream());
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
    } catch (ClassNotFoundException CNFExeption) {
      System.out.println("This exception should never been thrown, something is wrong");
      CNFExeption.printStackTrace();
    }
    return null;
  }
}
