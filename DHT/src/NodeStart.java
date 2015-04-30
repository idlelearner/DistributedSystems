import java.io.IOException;
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
		if (args.length != 1) {
			// throw error message that there should be atleast 2 arguments
			// the argument that the user needs to pass is the node number -
			// node00, node01, etc
			// we will not need the port anymore, the node number will
			// distinguish between 2 nodes
			// even on the same host
		}
		String hostName;
		String nodeNum = args[0];

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
			System.out.println("Looking up for MainNode..");
			startingNode = (Node) Naming.lookup("//" + startingNodeURL);
		} catch (RemoteException ex) {
			ex.printStackTrace();
		} catch (NotBoundException ex) {
			System.out.println("Unable to find main node..so this is the first node");
		} catch (Exception genE) {
		}

		Boolean joinSuccess = false;
		
		if (startingNode == null) /* This is the first node */
		{
			try {
				// bind the starting node
				System.out.println("Creating the chord ring");
				System.out.println("Binding node-0");
				currentNode.create();
				Naming.bind(nodeNum + "Node", currentNode);
				// curNode = (Node) Naming.lookup("//" + startingNodeURL);
			} catch (RemoteException ex) {
				ex.printStackTrace();
			} catch (AlreadyBoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NodeAlreadyPresentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NodeNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// we will create the ring with this start node

		} else // node-0 is present, we need to send a join request
		{
			try {
				// Binding node0?Node
				while(!joinSuccess)
				joinSuccess = startingNode.join(currentNode);
				Naming.bind(nodeNum + "Node", currentNode);
				//send a join_done call to the starting node
				startingNode.join_done();
				System.out.println("Node joined!!");
			} catch (RemoteException ex) {
			} catch (AlreadyBoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {

			} catch (NullPointerException e) {
			}
		}
	}
}
