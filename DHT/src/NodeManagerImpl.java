import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class NodeManagerImpl extends UnicastRemoteObject implements NodeManager {

	protected ServerLogger log;

	public NodeManagerImpl() throws RemoteException {
		super();
		log = ServerLogger.getInstance();
	}

	@Override
	public String findNode(String key) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String insert(String key, String value) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String lookup(String key) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

}
