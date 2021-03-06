import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Node extends Remote {
	public NodeKey getNodeID() throws RemoteException;

	public void setNodeID(NodeKey nodeID) throws RemoteException;

	public NodeKey getPredecessor() throws RemoteException;

	public void setPredecessor(NodeKey predecessor) throws RemoteException;

	public NodeKey getSuccessor() throws RemoteException;

	public void setSuccessor(NodeKey successor) throws RemoteException;

	public ArrayList<FingerTableEntry> getFingerTable() throws RemoteException;

	public void setFingerTable(ArrayList<FingerTableEntry> fingerTable)
			throws RemoteException;

	public Map<String, Set<WordEntry>> getWordEntryMap()
			throws RemoteException;

	public Boolean getConnectionStatus() throws RemoteException;

	public void toggleConnectionStatus() throws RemoteException;

	public void setFinger(NodeKey node, int index) throws RemoteException;

	public void addWordEntryAtNodeKey(String node, WordEntry entry)
			throws RemoteException;

	public Set<WordEntry> getWordEntriesForNodeKey(NodeKey key)
			throws RemoteException;

	public void removeNodeKeyFromMap(NodeKey key) throws RemoteException;

	public void setWordEntriesForNodeKey(String key, Set<WordEntry> entries)
			throws RemoteException;
	
	public void removeEntriesForKey(NodeKey key) throws RemoteException;

	public Boolean join(Node freshNode) throws RemoteException;

	public Node findSuccessorNode(GenericKey id) throws RemoteException;

	public NodeKey findSuccessorNodeId(GenericKey id) throws RemoteException;

	public WordEntry getWordEntryGivenNodeKey(String key, WordKey wKey)
			throws RemoteException;

	public WordEntry getWordEntryGivenJustWordKey(WordKey wKey)
			throws RemoteException;

	public void addNewWordEntriesAtNodeKeys(
			Map<String, Set<WordEntry>> newEntries) throws RemoteException;

	public void removeWordEntriesGivenKey(String key)
			throws RemoteException;

	public int getCountOfWordEntriesInMap() throws RemoteException;

	public FingerTableEntry getFingerAtIndex(int index) throws RemoteException;

	public void create() throws NodeAlreadyPresentException,
			NodeNotFoundException, RemoteException;

	public NodeKey find_node(String word) throws RemoteException;

	public WordEntry lookup(String word) throws RemoteException;

	public void insert(String word, String meaning) throws RemoteException;

	public void addNewWordEntriesAtParticularNodeKey(String idKey,
			Set<WordEntry> entries) throws RemoteException;

	public void printChordRingInfo() throws RemoteException;

	public Node getNodeDetails() throws RemoteException;

	public void join_done() throws RemoteException;
}
