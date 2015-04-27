import java.net.MalformedURLException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Class to describe the Chord Ring
 * @author varun
 *
 */
public class Ring implements Remote {
	
	/**
	 * create a chord ring by introducing the first node
	 * @param node
	 */
	public static void createRing(Node node) 
			throws NodeAlreadyPresentException, NodeNotFoundException{
		
		try {
			if(node.getConnectionStatus()) {
				throw new NodeAlreadyPresentException("Node is already in the ring,check!");
			}else if (node.getNodeID() == -1) {
				throw new NodeNotFoundException("Node creation was unsuccessful!");
			}
			node.toggleConnectionStatus();
			//make itself the successor
			node.setSuccessor(node);
			//set the predecessor to NULL for this node
			node.setPredecessor(null);
			
		} catch (Exception e) {
			//server should log this exception
		}
	}
	
	/**
	 * introduces the node n1 to the chord ring which already has node n2
	 * predecessor of n1 -> null, successor will be queried and set
	 * @param Node n1
	 * @param Node n2
	 * @throws RemoteException
	 * @throws MalformedURLException
	 */
	public static void join(Node n1, Node n2) throws RemoteException {
		
		//set predecessor to n1 to null
		n1.setPredecessor(null);
		
		Node succNode = null; //currently we do not have it, will query
		
		try {
			succNode = n2.findSuccessor(n1.getNodeID());
		} catch (Exception e) {
			//Log this exception
		}
		
		//set this successor node 
		n1.setSuccessor(succNode);
		
		//set the successor we just got as the first in the fingerTable
		n1.setFinger(succNode, 0);
	}
	
	//Ask node to find the successor of the node with given key
	public static Node findSuccessorOfNode(Node node, int id) 
			throws RemoteException, MalformedURLException{
		//first get the predecessor
		Node tmpNode = findPredecessorOfNode(node, id);
		
		//get the first successor of this predecessor node
		int tmpSuccId = tmpNode.getFirstSuccessor();
		
		java.rmi.registry.Registry remote = java.rmi.registry.LocateRegistry.getRegistry("");
		
		try {
			return (Node) Naming.lookup();
		}catch (RemoteException e) {
			
		}
		
		//we were not able to locate it, so return null
		return null;
	}
	
	public static Node findPredecessorOfNode(Node n, int id) {
		
	}
	
	public static Node findClosestPrecedingFingerOfNode() {
		
	}
}