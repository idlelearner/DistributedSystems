import java.io.Serializable;

/**
 * class for the NodeKey which is part of every node and used to hash into the chord ring
 * for the node key, the hashing is done on hostname and port
 * in this way we can have multiple nodes in one physical machine on different ports (processes)
 * @author varun
 *
 */
public class NodeKey extends GenericKey implements Serializable {
	private byte[] hashKey;
	private String host;
	private String port;
	private String stringForHashKey;
	
	public NodeKey(String host, String port) {
		this.host = host;
		this.port = port;
		this.hashKey = HashingHelper.hash((host + "|" + port).getBytes());
		this.stringForHashKey = hexString();
	}
	
	public String getHost() {
		return this.host;
	}
	
	public String getPort() {
		return this.port;
	}
	
	public String getStringForHashKey() {
		return this.stringForHashKey;
	}
	
	public byte[] getByteKey() {
		return this.hashKey;
	}
	
	@Override
	public boolean equals(GenericKey k) {
		return this.hashKey.equals(k.getByteKey());
	}
	
	@Override
	public String toString() {
		return getHost() + "|" + getPort();
	}
	
		
	
	private String hexString() {
		String str = "";
		String temp;
		for (byte b : hashKey) {
			temp = Integer.toHexString(b & 0xff).toUpperCase();
			
			if(temp.length() == 1) temp = "0" + temp;
			
			str += temp;
		}
		
		return str;
	}
}