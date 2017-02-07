package ClientServer.server;

import rmi.RMIException;

public interface PingPongServer {
	public String ping(int idNumber) throws RMIException;
}