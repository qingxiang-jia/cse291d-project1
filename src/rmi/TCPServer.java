package rmi;

import java.net.InetAddress;
import java.net.ServerSocket;

/**
 * A multi-thread TCP server the skeleton will use.
 * ref http://tutorials.jenkov.com/java-multithreaded-servers/multithreaded-server.html
 */
public class TCPServer implements Runnable {
  InetAddress myAddress;
  ServerSocket serverSocket;

  public TCPServer(InetAddress serverAddr) {
    myAddress = serverAddr;
  }

  public void run() {
    // open serverSocket

  }
}
