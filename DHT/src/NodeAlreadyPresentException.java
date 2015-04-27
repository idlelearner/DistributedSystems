/**
 * exception for when node is already present and we try to re-add it
 * @author varun
 *
 */

public class NodeAlreadyPresentException extends Exception {
	public NodeAlreadyPresentException() {
		super ();
	}
	
	public NodeAlreadyPresentException(String msg) {
		super(msg);
	}
}