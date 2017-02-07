package ClientServer.server;

public class PingPongServerImpl implements PingPongServer {

  @Override
  public String ping(int i) {
    return "Pong " + i;
  }
}
