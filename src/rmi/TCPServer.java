package rmi;

import sun.tools.jconsole.Worker;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;

/**
 * A multi-thread TCP server the skeleton will use. ref
 * http://tutorials.jenkov.com/java-multithreaded-servers/multithreaded-server.html
 */
public class TCPServer implements Runnable {
  int serverPort;
  Set<Thread> threads;
  InetAddress serverAddress;
  ServerSocket serverSocket;
  static TCPServer root;
  boolean stopped = false;

  public TCPServer(int serverPort) {
    initServer(serverPort);
  }

  private void initServer(int serverPort) {
    threads = new HashSet<>();
    root = this;
  }

  public TCPServer(String serverIP, int serverPort) {
    try {
      this.serverAddress = InetAddress.getByName(serverIP);
    } catch (UnknownHostException e) {
      System.out.println("Unknown host, server exiting");
      System.exit(-1);
    }
    initServer(serverPort);
  }

  public void run() {
    // open serverSocket that listens to incoming connections
    initServerSocket();
    while(!isStopped()) {
      Socket clientSocket = null;
      try {
        clientSocket = this.serverSocket.accept();
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

  private void initServerSocket() {
    try {
      this.serverSocket = new ServerSocket(this.serverPort);
    } catch (IOException e) {
      System.out.println("Open serverSocket at port " + this.serverSocket + " failed, exiting");
      System.exit(-1);
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
