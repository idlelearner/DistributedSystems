import java.net.ConnectException;
import java.net.MalformedURLException;
import java.rmi.AccessException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.Set;

/**
 * Class to describe the Chord Ring and its functions
 * All rmi calls are made from here
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
			}else if (node.getNodeID() == null) {
				throw new NodeNotFoundException("Node creation was unsuccessful!");
			}
			node.toggleConnectionStatus();
			//make itself the successor
			node.setSuccessor(node.getNodeID());
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
			succNode = n2.findSuccessorNode(n1.getNodeID());
		} catch (Exception e) {
			//Log this exception
		}
		
		//set this successor node 
		n1.setSuccessor(succNode.getNodeID());
		
		//set the successor we just got as the first in the fingerTable
		n1.setFinger(succNode.getNodeID(), 0);
	}
	
	//Ask node to find the successor of the node with given key
	public static Node findSuccessorOfNode(Node node, GenericKey id) 
			throws RemoteException, MalformedURLException{
		//first get the predecessor
		Node tmpNode = findPredecessorOfNode(node, id);
		
		//get the first successor of this predecessor node
		NodeKey tmpSucc = tmpNode.getSuccessor();
		
		//TODO : how do I do this?
		java.rmi.registry.Registry remote = java.rmi.registry.LocateRegistry.getRegistry("rmi:/" + );
		
		try {
			//TODO : How do I do this ?
			return (Node) Naming.lookup();
		}catch (RemoteException e) {
			
		}
		
		//we were not able to locate it, so return null
		return null;
	}
	
	public static Node findPredecessorOfNode(Node n, GenericKey id) {
		Node succ = null;
		Node startNode = n;
		//TODO : how do I do this?
		java.rmi.registry.Registry remote = java.rmi.registry.LocateRegistry.getRegistry("rmi:/" + );
		try {
			NodeKey succId = n.getSuccessor();
		}catch (Exception e) {
			//log
		}
		
		Node tempNode = null;
		
		int jumps = 0;
		
		//TODO: change this condition to check until id is between tempNode and tempNode.successor
		while() {
			if(tempNode != null)
				if(tempNode.getNodeID() == n.getNodeID()) break;
			
			jumps++;
			tempNode = n;
			
			n = (Node) findNearestPreceedingFinger(n, id);
			
			try {
				NodeKey refreshedId = n.getSuccessor();
				//TODO : How to do this RMI lookup
				succ = lookup;
			}catch (RemoteException e) {
				//Log this
			}
		}
		
		startNode.addJumps(jumps);
		return n;
	}
	
	public static Node findNearestPreceedingFinger(Node n, GenericKey id) {
		int fingerId;
		
		if(n.getFingerTableSize() == 0) return n;
		
		//traverse the finger table from the end
		for(int i = n.getFingerTableSize() - 1; i>=0 ; i--) {
			//return the first node that is between node and id
		}
		
		return n;
	}
	
	public static void redistributeKeys(Node node) throws RemoteException{
		Node thisNode = (Node) node;
		NodeKey predecessor = thisNode.getPredecessor();
		NodeKey successor = thisNode.getSuccessor();

		byte[] step = toByteArray(1);
		
		Node predecessorNode = null, successorNode = null;
		
		try //set predecessorNode
		{
			try
			{
				predecessorNode = (Node) Naming.lookup("/" + predecessor.getIP() + ":1099/" + String.valueOf(predecessor.getPID()));
			}
			catch (MalformedURLException ex)
			{
				//log
			}
		}
		catch (ConnectException ce)
		{
			//log
		}
		catch (NotBoundException ex)
		{
			//log
		}
		catch (AccessException ex)
		{
			//log
		}
		
		try //set successorNode
		{
			try
			{
				successorNode = (Node) Naming.lookup("/" + successor.getIP() + ":1099/" +String.valueOf(successor.getPID()));
			}
			catch (MalformedURLException ex)
			{
			}
		}
		catch (ConnectException ce)
		{
		}
		catch (NotBoundException ex)
		{
		}
		catch (AccessException ex)
		{
		}
		
		/*
		 * for every IdKey in the successor, pass them to successor
		 */
		Map<NodeKey, Set<WordEntry>> entries = node.getWordEntryMap();

		for (NodeKey counter : entries.keySet())
		{
			//add this to the map
		}

		successorNode.setPredecessor(predecessor);//successor's predecessor = node's predecessor
		predecessorNode.setSuccessor(successor);// set the successor
	}
	
	/**
	 * converts number's value to byteArray
	 */
	public static byte[] toByteArray(int number)
	{
		Integer num = (Integer) number;
		byte[] tempArray = new byte[1];
		tempArray[0] = num.byteValue();
		return tempArray;
	}
}