package pingPong.server;

import java.io.Serializable;

public class PingPongServerImpl implements PingPongServer, Serializable {

  @Override
  public String ping(int i) {
    return "Pong " + i;
  }
}
