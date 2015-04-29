import java.io.IOException;
import java.net.InetAddress;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Starts the node and joins the chord
 * 
 * @author thirunavukarasu
 *
 */
public class NodeStart {
	static NodeImpl currentNode;

	public static void main(String[] args) throws IOException,
			InterruptedException {
		if (args.length != 2) {
			// throw error message that there should be atleast 2 arguments
			// the argument that the user needs to pass is the node number -
			// node00, node01, etc
			// we will not need the port anymore, the node number will
			// distinguish between 2 nodes
			// even on the same host
		}
		String hostName;
		String nodeNum = args[1];

		int defaultPort = 1099;
		// get the host ip address
		hostName = Util.getHosttName();

		currentNode = new NodeImpl(new NodeKey(hostName, nodeNum));

		// Get node 0 from the registry
		Node startingNode = null;
		// Starting node lookup URL??
		String startingNodeURL = currentNode.getNodeID().getHost() + ":"
				+ defaultPort + "/node00Node";
		try {
			startingNode = (Node) Naming.lookup(startingNodeURL);
		} catch (RemoteException ex) {
		} catch (NotBoundException ex) {
		} catch (Exception genE) {
		}

		if (startingNode == null) /* This is the first node */
		{
			try {
				// bind the starting node
				Naming.bind(nodeNum + "Node", currentNode);
			} catch (RemoteException ex) {
			} catch (AlreadyBoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				// we will create the ring with this start node
				currentNode.create();
			} catch (NodeAlreadyPresentException ex) {
				System.out.println("AlreadyConnectedException");
			} catch (NodeNotFoundException ex) {
				System.out.println("IDNotFoundException");
			}

		} else // node-0 is present, we need to send a join request
		{
			try {
				// Binding node0?Node
				Naming.bind(nodeNum + "Node", currentNode);
			} catch (RemoteException ex) {
			} catch (AlreadyBoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				startingNode.join(currentNode);
			} catch (NullPointerException e) {
			}
		}
	}
}
