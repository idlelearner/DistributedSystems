import java.security.MessageDigest;

public final class HashingHelper {
	private static String HASHING_ALGO = "SHA-1";
	private static MessageDigest md;
	
	public static byte[] hash(byte[] hashKey) {
		try {
			md = MessageDigest.getInstance(HASHING_ALGO);
		}catch (Exception e) {
			//log when there is an exception
		}
		
		md.reset();
		md.update(hashKey);
		
		return md.digest();
	}
}