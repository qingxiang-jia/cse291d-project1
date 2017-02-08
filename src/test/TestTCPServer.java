package test;

import rmi.TCPClient;
import rmi.TCPServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * A quick test to test if TCPServer and associated classes are implemented correctly.
 */
public class TestTCPServer {
  public void runTest() {
    int port = 8000;
    TCPServer tcpServer = new TCPServer(port);
    Thread serverThread = new Thread(tcpServer);
    serverThread.start();
    try {
      Thread.sleep(2000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("wake");
    TCPClient tcpClient = new TCPClient("localhost", port);
    tcpClient.send("message from client");
    String messageFromServer = (String) tcpClient.receive();
    System.out.println("Message from server: " + messageFromServer);
    tcpClient.stopClient();
    tcpServer.stopServer();
  }

  public static void main(String...args) {
//    TestTCPServer testTCPServer = new TestTCPServer();
//    testTCPServer.runTest();
    InetSocketAddress address = new InetSocketAddress(7070);
    Socket socket = new Socket();
    try {
      socket.connect(address);
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
}
