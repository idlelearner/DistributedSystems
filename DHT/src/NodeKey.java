import java.io.Serializable;

public class NodeKey implements Serializable {
	private byte[] hashKey;
	private String host;
	private String port;
	
	public NodeKey(String host, String port) {
		this.host = host;
		this.port = port;
		this.hashKey = HashingHelper.hash((host + "|" + port).getBytes());
	}
}