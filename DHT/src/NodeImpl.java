import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Implementing the Node interface
 * 
 * @author varun
 *
 */
public class NodeImpl extends UnicastRemoteObject implements Node, Serializable {
	private Boolean busyInJoin;
	private NodeKey nodeID;
	private NodeImpl curNode = null;
	private ArrayList<FingerTableEntry> fingerTable;
	private NodeKey predecessor;
	private NodeKey successor;
	private ArrayList<String> wordList;
	private ServerLogger log;
	// Every node object will maintain a mapping of NodeKey to actual word
	// entries (as a set)
	// This mapping is needed when re-distributing, so that entries can be moved
	// as a whole
	private Map<String, Set<WordEntry>> wordEntryMap;

	private Boolean isConnected = false;

	public NodeImpl(NodeKey id) throws RemoteException {
		busyInJoin = false;
		fingerTable = new ArrayList<FingerTableEntry>(32);
		nodeID = id;
		predecessor = null;
		successor = null;
		wordEntryMap = new HashMap<String, Set<WordEntry>>();
		wordList = new ArrayList<String>();
		log = ServerLogger.getInstance();
	}

	public void toggleBusyInJoin() {
		busyInJoin = !busyInJoin;
	}

	public NodeKey getNodeID() {
		return nodeID;
	}

	public void setNodeID(NodeKey nodeID) {
		this.nodeID = nodeID;
	}

	public NodeKey getPredecessor() {
		return predecessor;
	}

	public void setPredecessor(NodeKey predecessor) {
		this.predecessor = predecessor;
	}

	public NodeKey getSuccessor() {
		return successor;
	}

	public void setSuccessor(NodeKey successor) {
		this.successor = successor;
	}

	public ArrayList<FingerTableEntry> getFingerTable() {
		return fingerTable;
	}

	public void setFingerTable(ArrayList<FingerTableEntry> fingerTable) {
		this.fingerTable = fingerTable;
	}

	public Boolean getConnectionStatus() throws RemoteException {
		return isConnected;
	}

	public Map<String, Set<WordEntry>> getWordEntryMap() {
		return this.wordEntryMap;
	}

	public void toggleConnectionStatus() throws RemoteException {
		isConnected = !isConnected;
	}

	public void setFinger(NodeKey node, int index) throws RemoteException {

		// the max length is 160, from 0-159
		if (index > 159) {
			return;
		}

		if (node.equals(nodeID))
			return;

		if (fingerTable.size() == 0) {
			// this is the first entry into the finger table
			fingerTable.add(0, new FingerTableEntry(node, 0, 159));
			return;
		}

		// We do not need a redundant addition to the fingerTable, so first make
		// sure its not already there
		Iterator<FingerTableEntry> it = fingerTable.iterator();
		FingerTableEntry temp;
		while (it.hasNext()) {
			temp = it.next();
			if (temp.contains(node))
				return;
		}

		if (index != 0) {
			int j;
			FingerTableEntry entry = null;
			for (j = 0; j < fingerTable.size(); j++) {
				entry = fingerTable.get(j);
				if (entry.getStartElement() <= index
						&& entry.getEndElement() >= index)
					break;

			}
			j++;

			for (; j < fingerTable.size(); j++) {
				fingerTable.remove(j);
			}

			entry.setEndElement(index - 1);

			fingerTable.add(fingerTable.size(), new FingerTableEntry(node,
					index, 159));
		} else {
			for (int j = 0; j < fingerTable.size(); j++) {
				fingerTable.remove(j);
			}
			fingerTable.add(0, new FingerTableEntry(node, 0, 159));
		}

		// sort the finger table entries
	}

	public void removeFinger(NodeKey node) throws RemoteException {

		for (int i = 0; i < fingerTable.size(); i++) {
			if (fingerTable.get(i).equals(node)) {
				fingerTable.remove(i);
				break;
			}
		}
	}

	public void removeFingerAtIndex(int i) throws RemoteException {
		fingerTable.remove(i);
	}

	public void removeAllFingers() throws RemoteException {
		for (int i = 0; i < fingerTable.size(); i++) {
			fingerTable.remove(i);
		}
	}

	public List<NodeKey> getFingerNodeIds() throws RemoteException {
		List<NodeKey> list = new ArrayList<NodeKey>();
		for (int i = 0; i < fingerTable.size(); i++)
			list.add(i, fingerTable.get(i).getNodeId());

		return list;
	}

	public void addWordEntryAtNodeKey(String hashKey, WordEntry entry)
			throws RemoteException {
		Set<WordEntry> entrySet;
		synchronized (this.wordEntryMap) {
			// if the nodeKey is already present, then we just add to the set,
			// else create new set
			boolean alreadyPresent = false;
			Iterator<Map.Entry<String, Set<WordEntry>>> it = wordEntryMap
					.entrySet().iterator();
			Map.Entry<String, Set<WordEntry>> itE = null;

			// lets traverse through the map
			while ((it.hasNext())) {
				itE = it.next();
				if (itE.getKey().equals(hashKey)) {
					alreadyPresent = true;
					break;
				}
			}

			// entries for this id are already present in the map, so add to the
			// set
			if (alreadyPresent) {
				entrySet = itE.getValue();
				log.write("Adding word to dictionary: " + entry);
				entrySet.add(entry);
			} else {
				// create a new set and introduce a new NodeKey/Set to the map
				entrySet = new TreeSet<WordEntry>();
				entrySet.add(entry);
				this.wordEntryMap.put(hashKey, entrySet);
				log.write("Adding word to dictionary: " + entry);
			}
		}

		// yay, we are done
	}

	// get the word entries for a given node key for this node
	public Set<WordEntry> getWordEntriesForNodeKey(NodeKey key)
			throws RemoteException {
		log.write("Gettign word entries for " + key);
		synchronized (this.wordEntryMap) {
			if (!this.wordEntryMap.containsKey(key)) {
				return null;
			} else {
				return this.wordEntryMap.get(key);
			}
		}
	}

	public void removeNodeKeyFromMap(NodeKey key) throws RemoteException {
		log.write("Remove node keys : " + key);
		synchronized (this.wordEntryMap) {
			this.wordEntryMap.remove(key);
		}
	}

	public void setWordEntriesForNodeKey(String key, Set<WordEntry> entries)
			throws RemoteException {
		synchronized (this.wordEntryMap) {
			this.wordEntryMap.put(key, entries);
		}
	}

	public void removeEntriesForKey(NodeKey key) throws RemoteException {
		log.write("Remove word entry keys : " + key);
		synchronized (this.wordEntryMap) {
			this.wordEntryMap.remove(key);
		}
	}

	public Boolean join(Node freshNode) throws RemoteException {
		log.write("Join new node : " + freshNode);
		if (busyInJoin) {
			log.write("Node-0 busy, cannot join");
			return false;
		}
		// set it to busy
		toggleBusyInJoin();
		try {
			Ring.join(freshNode, this);
		} catch (Exception e) {
			// log the exception
			e.printStackTrace();
		}

		return true;
	}

	public void join_done() throws RemoteException {
		toggleBusyInJoin();
		log.write("Node-0, join done!");
	}

	public Node findSuccessorNode(GenericKey id) {
		Node retNode = null;
		// System.out.println("In find successor node");
		log.write("In find successor node " + id);
		try {
			retNode = Ring.findSuccessorOfNode(this, id);
		} catch (Exception e) {
			// log the exception
			e.printStackTrace();
		}
		return retNode;
	}

	public NodeKey findSuccessorNodeId(GenericKey id) throws RemoteException {
		log.write("In find successor node ID " + id);
		NodeKey retNode = null;
		try {
			retNode = Ring.findSuccessorOfNode(this, id).getNodeID();
		} catch (Exception e) {
			// log the exception
			e.printStackTrace();
		}
		return retNode;
	}

	public WordEntry getWordEntryGivenNodeKey(String hashKey, WordKey wKey)
			throws RemoteException {
		Set<WordEntry> relevantSet = this.wordEntryMap.get(hashKey);
		log.write("Get node entry for NodeKey" + hashKey + "wordKey : " + wKey);
		Iterator<WordEntry> it = relevantSet.iterator();

		while (it.hasNext()) {
			WordEntry temp = it.next();
			if (temp.getWKey().equals(wKey)) {
				return temp;
			}
		}
		return null;
	}

	public WordEntry getWordEntryGivenJustWordKey(WordKey wKey)
			throws RemoteException {
		log.write("Get word entry given WordKey" + wKey);
		for (Map.Entry<String, Set<WordEntry>> entry : this.wordEntryMap
				.entrySet()) {
			WordEntry found = getWordEntryGivenNodeKey(entry.getKey(), wKey);
			if (found != null) {
				return found;
			}
		}
		return null;
	}

	public Map<String, Set<WordEntry>> giveEntries(NodeKey successorId)
			throws RemoteException {

		log.write("Get entries for successor ID : " + successorId);

		synchronized (this.wordEntryMap) {
			Map<String, Set<WordEntry>> temporaryMap = new HashMap<String, Set<WordEntry>>();
			WordEntry entry = null;
			Map.Entry<String, Set<WordEntry>> itEntry;
			Iterator<WordEntry> iter;
			Set<WordEntry> set;
			Iterator<Map.Entry<String, Set<WordEntry>>> it = this.wordEntryMap
					.entrySet().iterator();

			// iterate through the entire Map
			while (it.hasNext()) {
				// initialize a mpa for every IdKey
				set = new HashSet<WordEntry>();
				itEntry = it.next();
				iter = itEntry.getValue().iterator();

				// iterate through the entire set of the IdKey
				while (iter.hasNext()) {
					entry = iter.next();

					// if id is not in (newSuccessor, successor], then it should
					// be moved to successor
					// (it belongs to (predecessor, newSuccessor] part of the
					// entries)
					if (!GenericKey.isBetweenSuccessor(entry.getWKey(),
							successorId, this.nodeID))/*
													 * (itEntry.getKey().getHashKey
													 * (
													 * ).compareTo(predecessorID
													 * .getHashKey()) <= 0)
													 */
					{
						// add to the set the FIDEntry that corresponds to the
						// above
						set.add(entry);
					}
				}

				// add the set of the entries to the returning map
				if (!set.isEmpty()) {
					temporaryMap.put(itEntry.getKey(), set);
				}

				// delete the entries of the set, from the node's Set
				itEntry.getValue().removeAll(set);
			}
			return temporaryMap;
		}
	}

	public void addNewWordEntriesAtNodeKeys(
			Map<String, Set<WordEntry>> newEntries) throws RemoteException {
		log.write("Add new word entries at node keys : ");
		synchronized (this.wordEntryMap) {
			Iterator<Map.Entry<String, Set<WordEntry>>> it = newEntries
					.entrySet().iterator();
			Map.Entry<String, Set<WordEntry>> itEntry;
			Iterator<WordEntry> iter;
			WordEntry entry;

			// iterate the entire map
			while (it.hasNext()) {
				itEntry = it.next();
				iter = itEntry.getValue().iterator();

				// for every id key, iterate in its set of entries
				while (iter.hasNext()) {
					entry = iter.next();
					// add the entries to the node's map
					this.addWordEntryAtNodeKey(itEntry.getKey(), entry);
				}
			}
		}
	}

	public void addNewWordEntriesAtParticularNodeKey(String idKey,
			Set<WordEntry> entries) {
		log.write("Add new word entries at node key : " + idKey);
		Set<WordEntry> nSet;
		synchronized (this.wordEntryMap) {
			nSet = entries;
			this.wordEntryMap.put(idKey, nSet);
		}
	}

	public void removeWordEntriesGivenKey(String key) throws RemoteException {
		log.write("Remove word entries at given node key : " + key);

		synchronized (this.wordEntryMap) {
			Iterator<Map.Entry<String, Set<WordEntry>>> it = this.wordEntryMap
					.entrySet().iterator();
			Map.Entry<String, Set<WordEntry>> itEntry = null;

			// iterate through the entire Map
			while (it.hasNext()) {
				itEntry = it.next();

				// check if any of the ids already in the map equal with idKey
				// if anyone does, then remove it and stop the repetition
				if (itEntry.getKey().equals(key)) {
					this.wordEntryMap.remove(itEntry.getKey());
					break;
				}
			}
		}
	}

	public boolean checkIfWordEntryPresentAtNodeKey(WordEntry fid, NodeKey id)
			throws RemoteException {
		log.write("Check if Word Entry : " + fid + "present at NodeKey : " + id);
		synchronized (this.wordEntryMap) {
			boolean contains = false;
			Iterator<Map.Entry<String, Set<WordEntry>>> it = this.wordEntryMap
					.entrySet().iterator();
			Map.Entry<String, Set<WordEntry>> itEntry = null;
			WordEntry entry = null;

			// iterate through the entire Map
			while (it.hasNext()) {
				itEntry = it.next();

				// check if any of the ids already in the map equal with id
				if (itEntry.getKey().equals(id)) {
					contains = true;
					break;
				}
			}

			// if the id existed in the map check if there was also the filename
			// present
			if (contains) {
				Iterator<WordEntry> iter = itEntry.getValue().iterator();
				while (iter.hasNext()) {
					entry = iter.next();

					// if it does, return true, else return false
					if (fid.equals(entry.getWKey())) {
						return true;
					}
				}
			} else {
				return false;
			}

			return false;
		}
	}

	public int getCountOfWordEntriesInMap() throws RemoteException {
		int count = 0;
		Iterator<Map.Entry<String, Set<WordEntry>>> it = this.wordEntryMap
				.entrySet().iterator();
		Map.Entry<String, Set<WordEntry>> entry;

		while (it.hasNext()) {
			entry = it.next();
			count += entry.getValue().size();
		}
		log.write("Number of word entries in map : " + count);
		return count;
	}

	public void create() throws NodeAlreadyPresentException,
			NodeNotFoundException, RemoteException {
		log.write("Creating initial chord ring : ");
		Ring.createRing(this);
		curNode = this;
	}

	public NodeKey find_node(String word) throws RemoteException {
		// System.out.println("In find node... ");
		log.write("In find node... word : " + word);
		GenericKey wKey = new WordKey(word);
		Node responsibleNode = null;
		try {
			responsibleNode = this.findSuccessorNode(wKey);
		} catch (Exception ex) {
		}

		return responsibleNode.getNodeID();
	}

	public WordEntry lookup(String word) throws RemoteException {
		log.write("Lookup word : " + word);
		WordKey wKey = new WordKey(word);
		WordEntry wordEntry = null;

		try {
			// request word from this node
			wordEntry = this.getWordEntryGivenJustWordKey(wKey);
		} catch (RemoteException ex) {
			ex.printStackTrace();
		}

		return wordEntry;

	}

	public void insert(String word, String meaning) throws RemoteException {
		WordEntry wEntry = new WordEntry(word, this.getNodeID().getHost(), this
				.getNodeID().getNodeNum(), meaning);
		try {
			addWordEntryAtNodeKey(wEntry.getWKey().getStringForHashKey(),
					wEntry);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		log.write("Insert word : " + word + " meaning : " + meaning);
		// System.out.println("Word map size() : " + wordEntryMap.size());
		// System.out.println("Successor : " + successor);
		wordList.add(word);
		// System.out.println("WordList size : " + wordList.size());
	}

	public FingerTableEntry getFingerAtIndex(int index) throws RemoteException {
		return fingerTable.get(index);
	}

	public int getFingerTableSize() throws RemoteException {
		return fingerTable.size();
	}

	/**
	 * this is a trial print function to just print on the system out Later we
	 * need to move this to the client logs, so it will change
	 */
	public void printChordRingInfo() {
		// start with the successor of this node
		NodeKey nextKey = this.getSuccessor();
		Node nextNode = null;

		System.out.println("Node Number : " + curNode.getNodeID().getNodeNum());

		while (!nextKey.equals(curNode.getNodeID())) {

			try {
				// get the successor node
				nextNode = Ring.findNodeWithGivenNodeKey(nextKey);
				System.out.println("Node Number : "
						+ this.getNodeID().getNodeNum());
				nextKey = nextNode.getSuccessor();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public Node getNodeDetails() throws RemoteException {
		log.write("Getting node details");
		return this;
	}
}
