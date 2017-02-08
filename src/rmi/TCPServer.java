package rmi;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

/**
 * A multi-thread TCP server the skeleton will use. ref
 * http://tutorials.jenkov.com/java-multithreaded-servers/multithreaded-server.html
 */
public class TCPServer<T> implements Runnable {
  static final int DEFAULT_BACKLOG = 50;
  Set<Thread> threads;
  InetSocketAddress serverAddress;
  ServerSocket serverSocket;
  Skeleton<T> skeleton;
  static final int STOPPED = 0;
  static final int RUNNING = 1;
  int state = STOPPED;

  public TCPServer() throws IOException {
    serverSocket = new ServerSocket(); // random port, random IP
    init();
  }

  public TCPServer(int port) { // not used by skeleton
    try {
      serverSocket = new ServerSocket(port); // random IP
    } catch (IOException e) {
      e.printStackTrace();
    }
    init();
  }

  public TCPServer(InetSocketAddress ipPort) throws IOException {
    serverSocket = new ServerSocket(ipPort.getPort(), DEFAULT_BACKLOG, ipPort.getAddress());
    init();
  }

  private void init() {
    threads = new HashSet<>();
    state = RUNNING;
    serverAddress = new InetSocketAddress(serverAddress.getAddress(), serverAddress.getPort()); // in case of restart
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
      Thread workerThread = new Thread(new TCPWorker(clientSocket, this));
      registerThread(workerThread);
      workerThread.start();
    }
    System.out.println("TCPServer stopped");
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

  public synchronized void restartServer() {
    try {
      serverSocket = new ServerSocket(serverAddress.getPort(), DEFAULT_BACKLOG, serverAddress.getAddress());
    } catch (IOException e) {
      e.printStackTrace();
    }
    state = RUNNING;
    run();
  }

  public void registerThread(Thread t) {
    threads.add(t);
  }

  public synchronized void deregisterThread(Thread t) {
    if (threads.contains(t)) {
      threads.remove(t);
    }
  }

  public void setSkeleton(Skeleton<T> skeleton) {
    this.skeleton = skeleton;
  }
}
