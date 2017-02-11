package pingPong.server;

import java.io.Serializable;
import java.net.InetSocketAddress;

import rmi.RMIException;
import rmi.Skeleton;

public class PingServerFactoryImpl implements PingServerFactory, Serializable {

  private static final int DEFAULT_PORT = 8000;
  
  @Override
  public PingPongServer makePingPongServer() {
    return new PingPongServerImpl();
  }

  public static void main(String[] args){
    int port = DEFAULT_PORT;
    if(args.length == 1)
      port = Integer.parseInt(args[0]);
    InetSocketAddress addr = new InetSocketAddress(port);
    Skeleton<PingServerFactory> ske = new Skeleton<>(PingServerFactory.class, new PingServerFactoryImpl(), addr);
    try {
      ske.start();
      System.out.println("PingServer starts running...");
      while(true);
    } catch (RMIException e) {
      System.out.println("RMIException happens... " + e.getMessage());
    }
  }
}
