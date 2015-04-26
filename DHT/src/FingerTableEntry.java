
/**
 * Contains the finger table entry
 * 
 * @author thirunavukarasu
 *
 */
public class FingerTableEntry {

	private int index;
	private Node node;

	public FingerTableEntry(int index, Node node) {
		this.index = index;
		this.node = node;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

}
