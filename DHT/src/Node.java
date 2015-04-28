import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.sun.org.apache.xpath.internal.operations.Bool;

/**
 * class to contain details about the node in the chord.
 * 
 * @author thirunavukarasu
 *
 */
public class Node extends UnicastRemoteObject{
	private NodeKey nodeID;
	private ArrayList<FingerTableEntry> fingerTable;
	private NodeKey predecessor;
	private NodeKey successor;
	//Every node object will maintain a mapping of NodeKey to actual word entries (as a set)
	//This mapping is needed when re-distributing, so that entries can be moved as a whole
	private Map<NodeKey, Set<WordEntry>> wordEntryMap;
	private int jumps = 0;
	
	private Boolean isConnected = false;
	
	public Node(NodeKey id) throws RemoteException{
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
	
	public Boolean getConnectionStatus() {
		return isConnected;
	}
	
	public Map<NodeKey, Set<WordEntry>> getWordEntryMap() {
		return this.wordEntryMap;
	}
	
	public void toggleConnectionStatus() {
		isConnected = !isConnected;
	}
	
	public Node findSuccessor(int id) {
		Node foundNode = null;
		
		try {
			foundNode = Ring.findSuccessorOfNode(this, id);
		}catch (Exception e) {
			//Log this exception in the server logs
		}
		
		return foundNode;
	}
	
	public void setFinger(NodeKey node, int index) {
		//the max length is 160, from 0-159
		if(index > 159) {
			return;
		}
		
		if(node.equals(nodeID)) return;
		
		if(fingerTable.size() == 0) {
			//this is the first entry into the finger table
			fingerTable.add(0, new FingerTableEntry(node, 0, 159));
			return;
		}
		
		Iterator<FingerTableEntry> it = fingerTable.iterator();
		FingerTableEntry temp;
		while(it.hasNext()){
			temp = it.next();
			if(temp.contains(node)) return;
		}
		
		if(index != 0) {
			int j;
			FingerTableEntry entry = null;
			for(j=0;j<fingerTable.size();j++) {
				entry = fingerTable.get(j);
				if(entry.getStartElement() <= index && entry.getEndElement() >= index) break;
				
			}
			j++;
			
			for(; j < fingerTable.size(); j++) {
				fingerTable.remove(j);
			}
			
			entry.setEndElement(index - 1);
			
			fingerTable.add(fingerTable.size(), new FingerTableEntry(node, index, 159));
		}else {
			for(int j=0;j<fingerTable.size(); j++) {
				fingerTable.remove(j);
			}
			fingerTable.add(0, new FingerTableEntry(node, 0 , 159));
		}
		
		//sort the finger table entries
	}
	
	public void removeFinger(NodeKey node) {
		for (int i = 0; i < fingerTable.size(); i++)
		{
			if (fingerTable.get(i).equals(node))
			{
				fingerTable.remove(i);
				break;
			}
		}
	}
	
	public void removeFingerAtIndex(int i) {
		fingerTable.remove(i);
	}
	
	public List<NodeKey> getFingers() {
		List<NodeKey> list = new ArrayList<NodeKey>();
		for(int i = 0; i < fingerTable.size(); i++) list.add(i, fingerTable.get(i).getNodeId());
		
		return list;
	}
	
	public void addWordEntry(NodeKey node, WordEntry entry) {
		Set<WordEntry> entrySet;
		synchronized (this.wordEntryMap) {
			//if the nodeKey is already present, then we just add to the set, else create new set
			boolean alreadyPresent = false;
			Iterator<Map.Entry<NodeKey, Set<WordEntry>>> it = wordEntryMap.entrySet().iterator();
			Map.Entry<NodeKey, Set<WordEntry>> itE = null;
			
			//lets traverse through the map
			while((it.hasNext())) {
				itE = it.next();
				if(itE.getKey().equals(node)) {
					alreadyPresent = true;
					break;
				}
			}
			
			//entries for this id are already present in the map, so add to the set
			if(alreadyPresent){
				entrySet = itE.getValue();
				entrySet.add(entry);
			}else {
				//create a new set and introduce a new NodeKey/Set to the map
				entrySet = new TreeSet<WordEntry>();
				entrySet.add(entry);
				this.wordEntryMap.put(node, entrySet);
			}
		}
		
		//yay, we are done
	}
	
	//get the word entries for a given node key for this node
	public Set<WordEntry> getWordEntries(NodeKey key) throws RemoteException{
		synchronized (this.wordEntryMap) {
			if(!this.wordEntryMap.containsKey(key)) {
				return null;
			}
			
			else {
				return this.wordEntryMap.get(key);
			}
		}
	}
	
	public void setWordEntriesForKey(NodeKey key, Set<WordEntry> entries) {
		synchronized (this.wordEntryMap) {
			this.wordEntryMap.put(key, entries);
		}
	}
	
	public void removeEntriesForKey(NodeKey key) throws RemoteException {
		synchronized (this.wordEntryMap) {
			this.wordEntryMap.remove(key);
		}
	}
	
	public void join(Node freshNode) throws RemoteException{
		try {
			Ring.join(this, freshNode);
		}catch (Exception e) {
			//log the exception
		}
	}
	
	public Node findSuccessorNode(NodeKey id) {
		try {
			return Ring.findSuccessorOfNode(this, id);
		}catch (Exception e) {
			//log the exception
		}
	}
	
	public java.rmi.registry.Registry getRMIHandle()
	{
		//get this from the client
	}
	
	public WordEntry getSpecificWordEntry(NodeKey nKey, WordKey wKey) {
		WordEntry we = null;
		return we;
	}
	
	public WordEntry getEntryByWordKey(WordKey wKey) {
		for (Map.Entry<NodeKey, Set<WordEntry>> entry : this.wordEntryMap.entrySet())
		{
			WordEntry found = getSpecificWordEntry(entry.getKey(), wKey);
			if (found != null)
			{
				return found;
			}
		}
		return null;
	}
	
	public void addJumps(int jumps) {
		this.jumps += jumps;
	}
	
	public int getFingerTableSize() {
		return fingerTable.size();
	}
}
