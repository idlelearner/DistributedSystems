import java.math.BigDecimal;

/**
 * class to contain details about the node in the chord.
 * 
 * @author thirunavukarasu
 *
 */
public class Node {
	private int nodeID;
	private String nodeURL;
	private BigDecimal hashcode;

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

}
