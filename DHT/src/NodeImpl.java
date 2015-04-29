import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.sun.org.apache.xpath.internal.operations.Bool;

/**
 * Implementing the Node interface
 * 
 * @author varun
 *
 */
public class NodeImpl implements Node {
	private NodeKey nodeID;
	private ArrayList<FingerTableEntry> fingerTable;
	private NodeKey predecessor;
	private NodeKey successor;
	// Every node object will maintain a mapping of NodeKey to actual word
	// entries (as a set)
	// This mapping is needed when re-distributing, so that entries can be moved
	// as a whole
	private Map<NodeKey, Set<WordEntry>> wordEntryMap;
	private int jumps = 0;

	private Boolean isConnected = false;

	public NodeImpl(NodeKey id) throws RemoteException {
		fingerTable = new ArrayList<FingerTableEntry>(32);
		nodeID = id;
		predecessor = null;
		successor = null;
		wordEntryMap = new HashMap<NodeKey, Set<WordEntry>>();
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

	public Map<NodeKey, Set<WordEntry>> getWordEntryMap() {
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

	public void addWordEntryAtNodeKey(NodeKey node, WordEntry entry)
			throws RemoteException {
		Set<WordEntry> entrySet;
		synchronized (this.wordEntryMap) {
			// if the nodeKey is already present, then we just add to the set,
			// else create new set
			boolean alreadyPresent = false;
			Iterator<Map.Entry<NodeKey, Set<WordEntry>>> it = wordEntryMap
					.entrySet().iterator();
			Map.Entry<NodeKey, Set<WordEntry>> itE = null;

			// lets traverse through the map
			while ((it.hasNext())) {
				itE = it.next();
				if (itE.getKey().equals(node)) {
					alreadyPresent = true;
					break;
				}
			}

			// entries for this id are already present in the map, so add to the
			// set
			if (alreadyPresent) {
				entrySet = itE.getValue();
				entrySet.add(entry);
			} else {
				// create a new set and introduce a new NodeKey/Set to the map
				entrySet = new TreeSet<WordEntry>();
				entrySet.add(entry);
				this.wordEntryMap.put(node, entrySet);
			}
		}

		// yay, we are done
	}

	// get the word entries for a given node key for this node
	public Set<WordEntry> getWordEntriesForNodeKey(NodeKey key)
			throws RemoteException {
		synchronized (this.wordEntryMap) {
			if (!this.wordEntryMap.containsKey(key)) {
				return null;
			}

			else {
				return this.wordEntryMap.get(key);
			}
		}
	}

	public void removeNodeKeyFromMap(NodeKey key) throws RemoteException {
		synchronized (this.wordEntryMap) {
			this.wordEntryMap.remove(key);
		}
	}

	public void setWordEntriesForNodeKey(NodeKey key, Set<WordEntry> entries)
			throws RemoteException {
		synchronized (this.wordEntryMap) {
			this.wordEntryMap.put(key, entries);
		}
	}

	public void removeEntriesForKey(NodeKey key) throws RemoteException {
		synchronized (this.wordEntryMap) {
			this.wordEntryMap.remove(key);
		}
	}

	public void join(Node freshNode) throws RemoteException {
		try {
			Ring.join(this, freshNode);
		} catch (Exception e) {
			// log the exception
		}
	}

	public Node findSuccessorNode(GenericKey id) {
		Node retNode = null;

		try {
			retNode = Ring.findSuccessorOfNode(this, id);
		} catch (Exception e) {
			// log the exception
		}

		return retNode;
	}

	public NodeKey findSuccessorNodeId(GenericKey id) throws RemoteException {
		NodeKey retNode = null;

		try {
			retNode = Ring.findSuccessorOfNode(this, id).getNodeID();
		} catch (Exception e) {
			// log the exception
		}

		return retNode;
	}

	// public java.rmi.registry.Registry getRMIHandle()
	// {
	// //get this from the client
	// }

	public WordEntry getWordEntryGivenNodeKey(NodeKey nKey, WordKey wKey)
			throws RemoteException {
		Set<WordEntry> relevantSet = this.wordEntryMap.get(nKey);

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
		for (Map.Entry<NodeKey, Set<WordEntry>> entry : this.wordEntryMap
				.entrySet()) {
			WordEntry found = getWordEntryGivenNodeKey(entry.getKey(), wKey);
			if (found != null) {
				return found;
			}
		}
		return null;
	}

	public Map<NodeKey, Set<WordEntry>> giveEntries(NodeKey successorId)
			throws RemoteException {

		synchronized (this.wordEntryMap) {
			Map<NodeKey, Set<WordEntry>> temporaryMap = new HashMap<NodeKey, Set<WordEntry>>();
			WordEntry entry = null;
			Map.Entry<NodeKey, Set<WordEntry>> itEntry;
			Iterator<WordEntry> iter;
			Set<WordEntry> set;
			Iterator<Map.Entry<NodeKey, Set<WordEntry>>> it = this.wordEntryMap
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

	public void addNewWordEntriesAtNodeKey(
			Map<NodeKey, Set<WordEntry>> newEntries) throws RemoteException {
		synchronized (this.wordEntryMap) {
			Iterator<Map.Entry<NodeKey, Set<WordEntry>>> it = newEntries
					.entrySet().iterator();
			Map.Entry<NodeKey, Set<WordEntry>> itEntry;
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

	public void removeWordEntriesGivenNodeKey(NodeKey id)
			throws RemoteException {
		Set<WordEntry> nSet;
		synchronized (this.wordEntryMap) {
			Iterator<Map.Entry<NodeKey, Set<WordEntry>>> it = this.wordEntryMap
					.entrySet().iterator();
			Map.Entry<NodeKey, Set<WordEntry>> itEntry = null;

			// iterate through the entire Map
			while (it.hasNext()) {
				itEntry = it.next();

				// check if any of the ids already in the map equal with idKey
				// if anyone does, then remove it and stop the repetition
				if (itEntry.getKey().equals(id)) {
					this.wordEntryMap.remove(itEntry.getKey());
					break;
				}
			}
		}
	}

	public boolean checkIfWordEntryPresentAtNodeKey(WordEntry fid, NodeKey id)
			throws RemoteException {
		Set<WordEntry> nSet;
		synchronized (this.wordEntryMap) {
			boolean contains = false;
			Iterator<Map.Entry<NodeKey, Set<WordEntry>>> it = this.wordEntryMap
					.entrySet().iterator();
			Map.Entry<NodeKey, Set<WordEntry>> itEntry = null;
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
		Iterator<Map.Entry<NodeKey, Set<WordEntry>>> it = this.wordEntryMap
				.entrySet().iterator();
		Map.Entry<NodeKey, Set<WordEntry>> entry;

		while (it.hasNext()) {
			entry = it.next();
			count += entry.getValue().size();
		}

		return count;
	}

	public void create() throws NodeAlreadyPresentException,
			NodeNotFoundException, RemoteException {

		Ring.createRing(this);
	}

	public NodeKey find_node(String word) throws RemoteException {
		GenericKey wKey = new WordKey(word);
		Node responsibleNode = null;
		try {
			responsibleNode = this.findSuccessorNode(wKey);
		} catch (Exception ex) {
		}

		return responsibleNode.getNodeID();
	}

	public WordEntry lookup(String word) throws RemoteException {
		WordKey wKey = new WordKey(word);
		WordEntry wordEntry = null;

		try {
			// request word from this node
			wordEntry = this.getWordEntryGivenJustWordKey(wKey);
		} catch (RemoteException ex) {
		}

		return wordEntry;

	}

	public void insert(String word, String meaning) throws RemoteException {
		WordEntry wEntry = new WordEntry(word, this.getNodeID().getHost(), this
				.getNodeID().getNodeNum(), meaning);
		try {
			addWordEntryAtNodeKey(this.getNodeID(), wEntry);
		} catch (RemoteException e) {
			//
		}
	}

	public void addJumps(int jumps) throws RemoteException {
		this.jumps += jumps;
	}

	public int getFingerTableSize() throws RemoteException {
		return fingerTable.size();
	}
}
