import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;

/**
 * Starts the node and joins the chord
 * 
 * @author thirunavukarasu
 *
 */
public class NodeStart {
	public static void main(String[] args) throws MalformedURLException,
			RemoteException, NotBoundException {
		// bind
		// pass the nodeID.

		if (args.length != 3) {
			throw new RuntimeException(
					"Pass nodeID, hostURL and port number as arguments");
		}

		int nodeID = Integer.parseInt(args[0]);
		String url = args[1];
		int port = Integer.parseInt(args[2]);
		System.setSecurityManager(new RMISecurityManager());
		String hostname = url + ":" + port;

		// check if its main node
		MainNodeManager mainNodeManager = (MainNodeManager) Naming.lookup("//"
				+ hostname + "/MainNodeManager");

		// else
		// call main node lookup to add itself
		// For other nodes
		Node node = null;
		while (node == null) {
			node = mainNodeManager.join(url);
			try {
				// if the main node is busy receive the wait message and call
				// node - 0
				// after sometime.
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		NodeManager nodeManager = new NodeManagerImpl(node);

		// then call the job done.
		mainNodeManager.joinDone(node.getNodeURL());
	}
}
