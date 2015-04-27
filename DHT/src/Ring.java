import java.rmi.Remote;
import java.rmi.RemoteException;

public class Ring implements Remote {
	/**
	 * Class to maintain the Chord Ring
	 * Add a Node
	 * Remove a Node
	 */
	
	//creates a 
	public static void createRingWithFirstNode(Node node) {
		try {
			if(node.getConnectionStatus()) {
				//TODO : Make specific exception for this
				throw new RemoteException("Node is already in the ring,check!");
			}else if (node.getNodeID() == -1) {
				//TODO : Make specific exception for this as well
				throw new RemoteException("Node creation was unsuccessful!");
			}
			node.toggleConnectionStatus();
			//make itself the successor, add code to Node implementation for this
			//set the predecessor to NULL for this node
			
			
		} catch (RemoteException e) {
			
		}
	}
	
	//to make the Node 1 join the chord ring in which node 2 is present
	public static void join(Node n1, Node n2) throws RemoteException{
		int succId;
		
		try {
			//TODO : add this function to the Node manager class
			succId = n2.findSuccessorIdFor(n1.getNodeID());
		} catch (RemoteException e) {
			
		}
		
		//add this succ to the list of successors in for the node in its list
		
		//clear the fingerTable for this node
		
		//set the successor we just got as the first in the fingerTable
	}
	
	//Ask node to find the successor of the node with given key
	public static Node findSuccessorOfNode(Node node, int id) {
		//first get the predecessor
		Node tmp = findPredecessorOfNode(node, id);
		
		//get the immediate successor of this node
		//TODO : implement this function
		int tmpSuccId = tmpNode.getFirstSuccessor();
		
		java.rmi.registry.Registry remote = java.rmi.registry.LocateRegistry.getRegistry("");
		
		try {
			return (Node) Naming.lookup();
		}catch (RemoteException e) {
			
		}
		
		//we were not able to locate it, so return null
		return null;
	}
	
	public static Node findPredecessorOfNode() {
		
	}
	
	public static Node findClosestPrecedingFingerOfNode() {
		
	}
}