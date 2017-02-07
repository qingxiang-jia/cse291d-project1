package rmi;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

/**
 * A multi-thread TCP server the skeleton will use. ref
 * http://tutorials.jenkov.com/java-multithreaded-servers/multithreaded-server.html
 */
public class TCPServer implements Runnable {
  int serverPort;
  InetAddress serverAddress;
  ServerSocket serverSocket;
  boolean stopped = false;

  public TCPServer(int serverPort) {
    this.serverPort = serverPort;
  }

  public TCPServer(String serverIP, int serverPort) {
    try {
      this.serverAddress = InetAddress.getByName(serverIP);
    } catch (UnknownHostException e) {
      System.out.println("Unknown host, server exiting");
      System.exit(-1);
    }
    this.serverPort = serverPort;
  }

  public void run() {
    // open serverSocket that listens to incoming connections
    initServerSocket();

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
}
