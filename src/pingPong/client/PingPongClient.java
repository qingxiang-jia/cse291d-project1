package pingPong.client;

import java.net.InetSocketAddress;

import pingPong.server.PingPongServer;
import pingPong.server.PingServerFactory;
import rmi.RMIException;
import rmi.Stub;

public class PingPongClient {
  private PingPongServer server;
  private static int DEFAULT_PORT = 8000;
  
  private final int PIN_TIME = 4;

  public PingPongClient(PingServerFactory factory) {
    try {
      this.server = factory.makePingPongServer();
      System.out.println("succeeded to get ping-pong server...");
    } catch (RMIException e) {
      System.out.println("failed to get ping-pong server...");
      System.out.println(e.getMessage());
    }
  }

  private void pingTest() {
    int completed = 0, failed = 0;
    for (int i = 1; i <= PIN_TIME; i++) {
      System.out.println("Ping " + i);
      try {
        System.out.println(server.ping(i));
        completed++;
      } catch (RMIException e) {
        failed++;
        System.out.println("failed to get \"Pong " + i + "\" from server...");
        System.out.println(e.getMessage() + "\n");
      }
    }
    System.out.println(completed + " Tests Completed, " + failed + " Tests Failed");
  }

  public static void main(String[] args) {
    runClient(args);
  }

  private static void runClient(String[] args) {
    int port = (args.length == 1) ? Integer.parseInt(args[0])
        : (args.length == 2) ? Integer.parseInt(args[1]) : DEFAULT_PORT;
    String hostName = (args.length == 2) ? args[0] : "localhost";
    InetSocketAddress address = new InetSocketAddress(hostName, port);
    PingPongClient client = new PingPongClient(Stub.create(PingServerFactory.class, address));
    client.pingTest();
  }
}
