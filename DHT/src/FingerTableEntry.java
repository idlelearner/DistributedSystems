import java.math.BigDecimal;

/**
 * Contains the finger table entry
 * 
 * @author thirunavukarasu
 *
 */
public class FingerTableEntry {

	private BigDecimal index;
	private Node node;

	public BigDecimal getIndex() {
		return index;
	}

	public void setIndex(BigDecimal index) {
		this.index = index;
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

}
