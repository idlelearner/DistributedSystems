import java.io.Serializable;

/**
 * class for the NodeKey which is part of every node and used to hash into the chord ring
 * for the node key, the hashing is done on hostname and port
 * in this way we can have multiple nodes in one physical machine on different ports (processes)
 * @author varun
 *
 */
public class NodeKey extends GenericKey implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ByteWrapper hashKey;
	private String host;
	private String nodeNum;
	private String stringForHashKey;
	
	public NodeKey(String host, String nodeNum) {
		this(HashingHelper.hash((host +"|"+nodeNum).getBytes()), host, nodeNum);
	}
	
	public NodeKey(ByteWrapper hashKey, String host, String nodeNum) {
		this.host = host;
		this.nodeNum = nodeNum;
		this.hashKey = hashKey;
		this.stringForHashKey = hashKey.toHexString();
	}
	
	public String getHost() {
		return this.host;
	}
	
	public String getNodeNum() {
		return this.nodeNum;
	}
	
	public String getStringForHashKey() {
		return this.stringForHashKey;
	}
	
	public byte[] getByteKey() {
		return this.hashKey.getBytes();
	}
	
	public ByteWrapper getHashKey()
	{
		return this.hashKey;
	}
	
	@Override
	public boolean equals(GenericKey k) {
		return this.hashKey.equals(k.getByteKey());
	}
	
	@Override
	public String toString() {
		return getHost() + "|" + getNodeNum();
	}
}