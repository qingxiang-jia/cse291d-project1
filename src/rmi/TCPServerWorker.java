package rmi;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * This is the actual server that handles each client. This runs in a separate thread.
 */
public class TCPServerWorker implements Runnable {
  Socket clientSocket;

  public TCPServerWorker(Socket clientSocket) {
    this.clientSocket = clientSocket;
  }

  public void run() {
    try {
      InputStream in = clientSocket.getInputStream();
      OutputStream out = clientSocket.getOutputStream();
      // run some logic here
      out.close();
      in.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
