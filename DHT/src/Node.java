import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * class to contain details about the node in the chord.
 * 
 * @author thirunavukarasu
 *
 */
public class Node {
	private int nodeID;
	private String nodeURL;
	private int port;
	private BigDecimal hashcode;
	// 160 Bit ID space so will have 160 entries
	private ArrayList<FingerTableEntry> fingerTable;
	private Node predecessor;
	private Node successor;

	public int getNodeID() {
		return nodeID;
	}

	public void setNodeID(int nodeID) {
		this.nodeID = nodeID;
	}

	public String getNodeURL() {
		return nodeURL;
	}

	public void setNodeURL(String nodeURL) {
		this.nodeURL = nodeURL;
	}

	public BigDecimal getHashcode() {
		return hashcode;
	}

	public void setHashcode(BigDecimal hashcode) {
		this.hashcode = hashcode;
	}

	public Node getPredecessor() {
		return predecessor;
	}

	public void setPredecessor(Node predecessor) {
		this.predecessor = predecessor;
	}

	public Node getSuccessor() {
		return successor;
	}

	public void setSuccessor(Node successor) {
		this.successor = successor;
	}

	public ArrayList<FingerTableEntry> getFingerTable() {
		return fingerTable;
	}

	public void setFingerTable(ArrayList<FingerTableEntry> fingerTable) {
		this.fingerTable = fingerTable;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

}
