package ClientServer.server;

public class PingServerFactoryImpl implements PingServerFactory {

	@Override
	public PingPongServer makePingPongServer(){
		return new PingPongServerImpl();
	}

}
