package rmi;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * A multi-thread TCP server the skeleton will use. ref
 * http://tutorials.jenkov.com/java-multithreaded-servers/multithreaded-server.html
 */
public class TCPServer<T> implements Runnable {
  static final int DEFAULT_BACKLOG = 50;
  ServerSocket serverSocket;
  Skeleton<T> skeleton;
  static final int STOPPED = 0;
  static final int RUNNING = 1;
  int state = STOPPED;

  public TCPServer(Skeleton skeleton) throws IOException {
    serverSocket = new ServerSocket(0); // random port, random IP
    this.skeleton = skeleton;
    init();
  }

  public TCPServer(int port, Skeleton skeleton) { // not used by skeleton
    this.skeleton = skeleton;
    try {
      serverSocket = new ServerSocket(port); // random IP
    } catch (IOException e) {
      e.printStackTrace();
    }
    init();
  }

  public TCPServer(InetSocketAddress ipPort, Skeleton skeleton) throws IOException {
    this.skeleton = skeleton;
    serverSocket = new ServerSocket(ipPort.getPort(), DEFAULT_BACKLOG, ipPort.getAddress());
    init();
  }

  private void init() {
    state = RUNNING;
    // in case of restart
    skeleton.tcpServerAddress = new InetSocketAddress(serverSocket.getInetAddress(), serverSocket.getLocalPort());
  }

  public void run() {
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
      Thread workerThread = new Thread(new TCPWorker(clientSocket, this));
      workerThread.start();
    }
  }

  private synchronized boolean isStopped() {
    return (state == STOPPED);
  }

  public synchronized void stopServer() {
    try {
      this.serverSocket.close();
    } catch (IOException e) {
      System.out.println("Failed to stop serverSocket");
      e.printStackTrace();
    }
    state = STOPPED;
  }

  public void setSkeleton(Skeleton<T> skeleton) {
    this.skeleton = skeleton;
  }
}
