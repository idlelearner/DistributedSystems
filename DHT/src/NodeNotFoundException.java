/**
 * exception for when node with given id is not found in the chord ring
 * @author varun
 *
 */

public class NodeNotFoundException extends Exception {
	public NodeNotFoundException() {
		super();
	}
	
	public NodeNotFoundException(String msg) {
		super(msg);
	}
}