import java.io.Serializable;

public class WordKey extends GenericKey implements Serializable {
	private String word;
	private ByteWrapper hashKey;
	private String stringForHashKey;
	
	public WordKey(String word) {
		this(HashingHelper.hash(word.getBytes()), word);
	}
	
	public WordKey(ByteWrapper hashKey, String word) {
		this.hashKey = hashKey;
		this.word = word;
		this.stringForHashKey = hashKey.toHexString();
	}
	
	public String getWord() {
		return word;
	}
	
	public ByteWrapper getHashKey() {
		return hashKey;
	}
	
	public String getStringForHashKey() {
		return this.stringForHashKey;
	}
	
	@Override
	public boolean equals(GenericKey k) {
		return this.hashKey.equals(k.getByteKey());
	}
	
	@Override
	public byte[] getByteKey() {
		return this.hashKey.getBytes();
	}
}