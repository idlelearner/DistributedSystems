
/**
 * Contains the finger table entry
 * 
 * @author thirunavukarasu
 *
 */
public class FingerTableEntry {

	private int index;
	private NodeKey nodeId;

	public FingerTableEntry(int index, NodeKey nodeId) {
		this.index = index;
		this.nodeId = nodeId;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public NodeKey getNodeId() {
		return nodeId;
	}

	public void setNodeId(NodeKey nodeId) {
		this.nodeId = nodeId;
	}

}
