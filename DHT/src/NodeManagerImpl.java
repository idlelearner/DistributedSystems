import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class NodeManagerImpl extends UnicastRemoteObject implements NodeManager {

	private ServerLogger log;
	private ArrayList<FingerTableEntry> fingerTable;
	private Node node;

	public NodeManagerImpl(Node node) throws RemoteException {
		super();
		log = ServerLogger.getInstance();
		fingerTable = new ArrayList();
		// TODO : Need to pass the
		this.node = node;
		
		//Register this node for lookup
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