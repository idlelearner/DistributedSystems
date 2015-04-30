import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public interface Node extends Remote{
	public NodeKey getNodeID() throws RemoteException;
	public void setNodeID(NodeKey nodeID) throws RemoteException;
	public NodeKey getPredecessor() throws RemoteException;
	public void setPredecessor(NodeKey predecessor) throws RemoteException;
	public NodeKey getSuccessor() throws RemoteException;
	public void setSuccessor(NodeKey successor) throws RemoteException;
	public ArrayList<FingerTableEntry> getFingerTable() throws RemoteException;
	public void setFingerTable(ArrayList<FingerTableEntry> fingerTable) throws RemoteException;
	public Map<NodeKey, Set<WordEntry>> getWordEntryMap() throws RemoteException;
	public Boolean getConnectionStatus() throws RemoteException;
	public void toggleConnectionStatus() throws RemoteException;
	public void setFinger(NodeKey node, int index) throws RemoteException;
	public void removeFinger(NodeKey node) throws RemoteException;
	public void removeFingerAtIndex(int i) throws RemoteException;
	public void removeAllFingers() throws RemoteException;
	public List<NodeKey> getFingerNodeIds() throws RemoteException;
	public void addWordEntryAtNodeKey(NodeKey node, WordEntry entry) throws RemoteException;
	public Set<WordEntry> getWordEntriesForNodeKey(NodeKey key) throws RemoteException;
	public void removeNodeKeyFromMap(NodeKey key) throws RemoteException;
	public void setWordEntriesForNodeKey(NodeKey key, Set<WordEntry> entries) throws RemoteException;
	public void removeEntriesForKey(NodeKey key) throws RemoteException;
	public void join(Node freshNode) throws RemoteException;
	public Node findSuccessorNode(GenericKey id) throws RemoteException;
	public NodeKey findSuccessorNodeId(GenericKey id) throws RemoteException;
	public WordEntry getWordEntryGivenNodeKey(NodeKey nKey, WordKey wKey) throws RemoteException;
	public WordEntry getWordEntryGivenJustWordKey(WordKey wKey) throws RemoteException;
	public Map<NodeKey, Set<WordEntry>> giveEntries(NodeKey successorId) throws RemoteException;
	public void addNewWordEntriesAtNodeKeys(Map<NodeKey, Set<WordEntry>> newEntries) throws RemoteException;
	public void removeWordEntriesGivenNodeKey(NodeKey id) throws RemoteException;
	public boolean checkIfWordEntryPresentAtNodeKey(WordEntry fid, NodeKey id) throws RemoteException;
	public int getCountOfWordEntriesInMap() throws RemoteException;
	public FingerTableEntry getFingerAtIndex(int index) throws RemoteException;
	public void create() throws NodeAlreadyPresentException, NodeNotFoundException, RemoteException;
	public NodeKey find_node(String word) throws RemoteException;
	public WordEntry lookup(String word) throws RemoteException;
	public void insert(String word, String meaning) throws RemoteException;
	public void addNewWordEntriesAtParticularNodeKey(NodeKey idKey, Set<WordEntry> entries) throws RemoteException;
	public void printChordRingInfo() throws RemoteException;
	
}
