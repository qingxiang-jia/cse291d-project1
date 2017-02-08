package rmi;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

/**
 * A multi-thread TCP server the skeleton will use. ref
 * http://tutorials.jenkov.com/java-multithreaded-servers/multithreaded-server.html
 */
public class TCPServer implements Runnable {
  int serverPort;
  Set<Thread> threads;
  ServerSocket serverSocket;
  static TCPServer root;
  boolean stopped = false;

  public TCPServer(int serverPort) {
    System.out.println("constructor called");
    threads = new HashSet<>();
    root = this;
    this.serverPort = serverPort;
    try {
      serverSocket = new ServerSocket(this.serverPort);
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }

  public void run() {
    while(!isStopped()) {
      Socket clientSocket = null;
      try {
        System.out.println("about to accept client connection");
        clientSocket = this.serverSocket.accept();
        System.out.println("accepted client connection");
      } catch (IOException e) {
        if (isStopped()) {
          System.out.println("Server has stopped");
          return;
        } else {
          System.out.println("Failed to accept client connection");
          e.printStackTrace();
        }
      }
      Thread workerThread = new Thread(new TCPWorker(clientSocket));
      registerThread(workerThread);
      workerThread.start();
    }
    System.out.println("TCPServer stopped");
  }

  private synchronized boolean isStopped() {
    return this.stopped;
  }

  public synchronized void stopServer() {
    this.stopped = true;
    try {
      this.serverSocket.close();
    } catch (IOException e) {
      System.out.println("Failed to stop serverSocket");
      e.printStackTrace();
    }
  }

  public void registerThread(Thread t) {
    threads.add(t);
  }

  public synchronized void deregisterThread(Thread t) {
    if (threads.contains(t)) {
      threads.remove(t);
    }
  }
}
