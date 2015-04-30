import java.net.ConnectException;
import java.net.MalformedURLException;
import java.rmi.AccessException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.Set;

/**
 * Class to describe the Chord Ring and its functions All rmi calls are made
 * from here
 * 
 * @author varun
 *
 */
public class Ring implements Remote {

	/**
	 * create a chord ring by introducing the first node
	 * 
	 * @param node
	 */
	public static int port = 1099;

	public static void createRing(Node node)
			throws NodeAlreadyPresentException, NodeNotFoundException {
		System.setSecurityManager(new RMISecurityManager());
		try {
			if (node.getConnectionStatus()) {
				throw new NodeAlreadyPresentException(
						"Node is already in the ring,check!");
			} else if (node.getNodeID() == null) {
				throw new NodeNotFoundException(
						"Node creation was unsuccessful!");
			}
			node.toggleConnectionStatus();
			// make itself the successor
			node.setSuccessor(node.getNodeID());
			// set the predecessor to NULL for this node
			node.setPredecessor(null);

		} catch (Exception e) {
			// server should log this exception
			e.printStackTrace();
		}
	}

	public static Node findNodeWithGivenNodeKey(NodeKey nKey)
			throws RemoteException {
		Node foundNode = null;
		try {
			// get the successor node
			foundNode = (Node) Naming.lookup("/" + nKey.getHost() + ":"
					+ Ring.port + "/" + nKey.getNodeNum() + "Node");
		} catch (RemoteException e) {

		} catch (MalformedURLException e) {

		} catch (NotBoundException e) {

		}

		return foundNode;
	}

	/**
	 * method called by a node that is possible predecessor to the current node
	 * checks whether predecessor of node is not set, or if the possible
	 * predecessor is closer to the current node than the current predecessor
	 * and updates(if needed) the predecessor variable
	 */
	public static void informNode(Node self, Node predecessor)
			throws RemoteException {
		/*
		 * checks if current predecessor is not set(null) and if
		 * possiblePredecessor's ID lies between (currentPredecessor's ID,
		 * currentNode's ID
		 */
		if (self.getPredecessor() == null
				|| GenericKey.isBetweenNotify(predecessor.getNodeID(),
						self.getPredecessor(), self.getNodeID())) // updates the
																	// local
																	// variable
																	// of
																	// current
																	// Node
		{
			self.setPredecessor(predecessor.getNodeID());
			predecessor.setSuccessor(self.getNodeID());
			redistributeKeys(self);
		}
	}

	/**
	 * introduces the node n1 to the chord ring which already has node n2
	 * predecessor of n1 -> null, successor will be queried and set
	 * 
	 * @param Node
	 *            n1
	 * @param Node
	 *            n2
	 * @throws RemoteException
	 * @throws MalformedURLException
	 */
	public static void join(Node n1, Node n2) throws RemoteException {

		// set predecessor to n1 to null
		n1.setPredecessor(null);

		Node succNode = null; // currently we do not have it, will query

		try {
			succNode = n2.findSuccessorNode(n1.getNodeID());
		} catch (Exception e) {
			// Log this exception
		}

		// set this successor node
		n1.setSuccessor(succNode.getNodeID());

		// set the successor we just got as the first in the fingerTable
		n1.setFinger(succNode.getNodeID(), 0);

		try {
			Node n = (Node) Naming.lookup("//" + n2.getNodeID().getHost() + ":"
					+ port + "/" + n2.getNodeID().getNodeNum() + "Node");
			informNode(n1, n);
		} catch (MalformedURLException e) {

		} catch (NotBoundException ex) {
		}
	}

	// Ask node to find the successor of the node with given key
	public static Node findSuccessorOfNode(Node node, GenericKey id)
			throws RemoteException, MalformedURLException {
		// first get the predecessor
		System.out.println("In find successor of Node");
		Node tmpNode = findPredecessorOfNode(node, id);

		// get the first successor of this predecessor node
		NodeKey tmpSucc = tmpNode.getSuccessor();

		try {
			return (Node) Naming.lookup("//" + tmpSucc.getHost() + ":" + port
					+ "/" + tmpSucc.getNodeNum() + "Node");
		} catch (RemoteException e) {

		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// we were not able to locate it, so return null
		return null;
	}

	public static Node findPredecessorOfNode(Node n, GenericKey id) {
		Node succ = null;
		Node startNode = n;
		NodeKey succId;
		try {
			succId = n.getSuccessor();
			succ = (Node) Naming.lookup("//" + succId.getHost() + ":" + port
					+ "/" + succId.getNodeNum() + "Node");

			Node tempNode = null;

			// TODO: change this condition to check until id is between tempNode
			// and tempNode.successor
			while (!GenericKey.isBetweenSuccessor(id, n.getNodeID(),
					n.getSuccessor())) {
				if (tempNode != null)
					if (tempNode.getNodeID() == n.getNodeID())
						break;

				tempNode = n;

				n = (Node) findNearestPreceedingFinger(n, id);

				try {
					NodeKey refreshedId = n.getSuccessor();
					// TODO : How to do this RMI lookup
					succ = (Node) Naming.lookup("//" + refreshedId.getHost()
							+ ":" + port + "/" + refreshedId.getNodeNum()
							+ "Node");
				} catch (RemoteException e) {
					// Log this
				}
			}
		} catch (Exception e) {
			// log
			e.printStackTrace();
		}

		return n;
	}

	public static Node findNearestPreceedingFinger(Node n, GenericKey id) {
		NodeKey fingerId;
		try {
			if (n.getFingerTable().isEmpty())
				return n;

			// traverse the finger table from the end
			for (int i = n.getFingerTable().size() - 1; i >= 0; i--) {
				// return the first node that is between node and id
				fingerId = n.getFingerAtIndex(i).getNodeId();

				if (fingerId == null) {
					continue;
				}

				// returns the first node that is between (node, id)
				if (GenericKey.isBetween(fingerId, n.getNodeID(), id)) {
					try {
						try {
							return (Node) Naming.lookup("//"
									+ fingerId.getHost() + ":" + port
									+ fingerId.getNodeNum() + "Node");
						} catch (MalformedURLException ex) {
						}
						// java.rmi.registry.Registry remote =
						// java.rmi.registry.LocateRegistry.getRegistry("rmi:/"
						// + node.getLocalID().getIP());
						// return (Node)
						// remote.lookup(String.valueOf(finger.getPID()));
					} catch (RemoteException ex) {
					} catch (NotBoundException ex) {
					}
				}
			}
		} catch (RemoteException e) {

		}

		return n;
	}

	/**
	 * redistribute keys when the new node is introduced into the ring
	 * it will pick up some keys from its predecessor, which keys ?
	 * those which were first with the predecessor but now their value is >= that of this new node
	 * @param node
	 * @throws RemoteException
	 */
	public static void redistributeKeys(Node node) throws RemoteException {
		Node thisNode = (Node) node;
		NodeKey predecessor = thisNode.getPredecessor();

		byte[] step = toByteArray(1);

		Node predecessorNode = null;

		try // set predecessorNode
		{
			try {
				predecessorNode = (Node) Naming.lookup("//"
						+ predecessor.getHost() + ":" + port + "/"
						+ predecessor.getNodeNum() + "Node");
			} catch (MalformedURLException ex) {
				// log
			}
		} catch (NotBoundException ex) {
			// log
		} catch (AccessException ex) {
			// log
		}

		/*
		 * for every key in the predecessor which is >= new nodes key, pass them to this node
		 */
		Map<NodeKey, Set<WordEntry>> entries = predecessorNode.getWordEntryMap();

		for (NodeKey counter : entries.keySet()) {
			//if this is greater than or equal to that of this node's key, take these word entries in
			if(!GenericKey.isBetween(counter, predecessor, node.getNodeID()))
				node.addNewWordEntriesAtParticularNodeKey(counter,entries.get(counter));
		}
	}

	/**
	 * converts number's value to byteArray
	 */
	public static byte[] toByteArray(int number) {
		Integer num = (Integer) number;
		byte[] tempArray = new byte[1];
		tempArray[0] = num.byteValue();
		return tempArray;
	}
}