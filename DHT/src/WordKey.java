import java.io.Serializable;

public class WordKey extends GenericKey implements Serializable {
	private String word;
	private byte[] hashKey;
	private String stringForHashKey;
	
	public WordKey(String word) {
		this(HashingHelper.hash(word.getBytes()), word);
	}
	
	public WordKey(byte[] key, String word) {
		this.hashKey = key;
		this.word = word;
		this.stringForHashKey = this.hexString();
	}
	
	public String getWord() {
		return word;
	}
	
	public byte[] getHashKey() {
		return hashKey;
	}
	
	public String getStringForHashKey() {
		return this.stringForHashKey;
	}
	
	@Override
	public boolean equals(GenericKey k) {
		return this.hashKey.equals(k.getByteKey());
	}
	
	public byte[] getByteKey() {
		return this.hashKey;
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