import java.math.BigDecimal;
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

	public MainNodeManagerImpl(String url, int port) throws RemoteException {
		super();
		log = ServerLogger.getInstance();
		activeNodes = new ArrayList<Node>();
		ongoingNodes = new ArrayList<Node>();
		fingerTable = new ArrayList<>();
		free = true;
		initMasterNode(url, port);
	}

	private void initMasterNode(String url, int port) {
		// Construct the current node
		node = new Node();
		node.setNodeURL(url);
		node.setPort(port);
		BigDecimal masterNodeHashCode = Util.getHashCode(url + port);
		node.setHashcode(masterNodeHashCode);

		// Build the finger Table
		for (int i = 0; i < 160; i++) {
			fingerTable.add(new FingerTableEntry(i, node));
		}
		
		node.setPredecessor(node);
		node.setPredecessor(node);
	}

	public static void main(String[] args) throws RemoteException {

		String url = args[0];
		int port = Integer.parseInt(args[1]);
		MainNodeManager mainNodeManager = new MainNodeManagerImpl(url, port);

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
