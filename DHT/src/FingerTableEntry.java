
/**
 * Contains the finger table entry
 * 
 * @author thirunavukarasu
 *
 */
public class FingerTableEntry implements Comparable<FingerTableEntry>{

	private NodeKey nodeId;
	private int startElement;
	private int endElement;

	public FingerTableEntry(NodeKey key, int se, int ee) {
		this.nodeId = key;
		this.startElement = se;
		this.endElement = ee;
	}

	public NodeKey getNodeId() {
		return nodeId;
	}

	public void setNodeId(NodeKey nodeId) {
		this.nodeId = nodeId;
	}
	
	public int getEndElement() {
		return this.endElement;
	}

	public void setEndElement(int ee){
		this.endElement = ee;
	}
	
	public int getStartElement() {
		return this.startElement;
	}
	
	public void setStartElement(int se) {
		this.startElement = se;
	}
	
	public int compareTo(FingerTableEntry o) {
//		if(this.endElement > o.endElement) return 1;
//		if(this.endElement < o.endElement) return -1;
		return Integer.compare(this.endElement, o.endElement);
	}
	
	public boolean contains(NodeKey key) {
		return this.nodeId.equals(key);
	}
}
