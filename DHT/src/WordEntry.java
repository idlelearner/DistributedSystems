import java.io.Serializable;

/*
 * Class to capture a word entry
 * @varun
 */
public class WordEntry implements Serializable, Comparable<WordEntry> {
	private GenericKey wKey; //this is the word key, which contains the hash of the word
	private GenericKey nKey; //this is the node key, which contains the hash of the node
	private String meaning; // meaning of the work which will be looked up
	
	public WordEntry(String word, String host, String port, String meaning) {
		this.wKey = new WordKey(word);
		this.nKey = new NodeKey(HashingHelper.hash((host + "|" + port).getBytes()), host, port);
		this.meaning = meaning;
	}
	
	public GenericKey getWKey() {
		return this.wKey;
	}
	
	public GenericKey getNKey() {
		return this.nKey;
	}
	
	public String getMeaning() {
		return this.meaning;
	}
	
	public ByteWrapper getHashKey()
	{
		return wKey.getHashKey();
	}

	public ByteWrapper getHashedIDKey()
	{
		return new ByteWrapper(nKey.getByteKey());
	}
	
	public byte[] getByteKey()
	{
		ByteWrapper hashKey = new ByteWrapper(wKey.getByteKey());
		return hashKey.getBytes();
	}
	
	public boolean equals(GenericKey k) {
		return this.wKey.equals(k);
	}
	
	public int compareTo(WordEntry o) {
			WordEntry entry = (WordEntry) o;
			return this.wKey.getHashKey().compareTo(entry.getHashKey());
	}
}