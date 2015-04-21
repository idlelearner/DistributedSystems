import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


//Implementation of the server class. Functions defined in the MainNodeManager interface implemented
public class MainNodeManagerImpl extends UnicastRemoteObject implements
		MainNodeManager {
	protected ServerLogger log;

	public MainNodeManagerImpl() throws RemoteException {
		super();
		log = ServerLogger.getInstance();
	}

	@Override
	public void join(String url) throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void joinDone(String url) throws RemoteException {
		// TODO Auto-generated method stub

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
