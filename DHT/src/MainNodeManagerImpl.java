import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

//Implementation of the server class. Functions defined in the MainNodeManager interface implemented
public class MainNodeManagerImpl extends UnicastRemoteObject implements
		MainNodeManager {
	private ServerLogger log;
	private ArrayList<Node> activeNodes;
	private ArrayList<Node> ongoingNodes;
	private ArrayList<FingerTableEntry> fingerTable;
	private boolean free;
	private Node node;

	public MainNodeManagerImpl(String url, String port) throws RemoteException {
		super();
		log = ServerLogger.getInstance();
		activeNodes = new ArrayList<Node>();
		ongoingNodes = new ArrayList<Node>();
		fingerTable = new ArrayList<>();
		free = true;
		initMasterNode();
	}

	private void initMasterNode() {
		
	}

	public static void main(String[] args) throws RemoteException {
		
		
//		MainNodeManager mainNodeManager = new MainNodeManagerImpl();
		
		
	}
	
	@Override
	public Node join(String url) throws RemoteException {
		// TODO Auto-generated method stub
		free = false;
		if (!free) {
			// return "wait" or null;
			return null;
		} else {

			free = true;

			return new Node();
		}
	}

	@Override
	public void joinDone(String url) throws RemoteException {
		// TODO Auto-generated method stub

		// add the url to the available nodes.

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
