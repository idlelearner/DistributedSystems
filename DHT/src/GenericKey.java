/**
 * structure for a generic key (abstract class)
 * word key and node key will extend this class
 * @author varun
 *
 */
public abstract class GenericKey {
	public abstract byte[] getByteKey();
	public abstract boolean equals(GenericKey k);
	public abstract String getStringForHashKey();
}