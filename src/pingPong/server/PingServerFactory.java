package pingPong.server;

import rmi.RMIException;

public interface PingServerFactory {
  public PingPongServer makePingPongServer() throws RMIException;
}
