package rmi;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * A simple TCP client used to test TCPServer and demonstrate usage.
 */
public class TCPClient {
  Socket socketToServer;
  ObjectOutputStream out;
  ObjectInputStream in;
  String serverIP;
  int serverPort;

  public TCPClient(String serverIP, int serverPort) {
    this.serverIP = serverIP;
    this.serverPort = serverPort;
    initClient();
  }

  public void initClient() {
    try {
      socketToServer = new Socket(serverIP, serverPort);
      out = new ObjectOutputStream(socketToServer.getOutputStream());
      out.flush();
      in = new ObjectInputStream(socketToServer.getInputStream());
    } catch (IOException ioe) {
      ioe.printStackTrace();
      stopClient();
    }
  }

  public void send(Object obj) {
    try {
      out.writeObject(obj);
    } catch (IOException ioe) {
      System.out.println("Failed to send obj to server, exiting");
    }
  }

  public Object receive() {
    Object obj = null;
    try {
      obj = in.readObject();
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }
    return obj;
  }

  public void stopClient() {
    try {
      socketToServer.close();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }
}
