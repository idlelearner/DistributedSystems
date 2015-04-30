/**
 * structure for a generic key (abstract class) word key and node key will
 * extend this class
 * 
 * @author varun
 *
 */
public abstract class GenericKey {
	public abstract byte[] getByteKey();

	public abstract boolean equals(GenericKey k);

	public abstract ByteWrapper getHashKey();

	public abstract String getStringForHashKey();

	public static boolean isBetweenSuccessor(GenericKey id, GenericKey first,
			GenericKey last) {
		ByteWrapper idBig = id.getHashKey();
		ByteWrapper firstBig = first.getHashKey();
		ByteWrapper lastBig = last.getHashKey();

		if (firstBig.compareTo(lastBig) == 1) {
			if (idBig.compareTo(firstBig) == -1
					&& idBig.compareTo(lastBig) <= 0) {
				return true;
			}

			if (idBig.compareTo(firstBig) == 1) {
				return true;
			}
		}

		if (firstBig.compareTo(lastBig) == -1) {
			if (idBig.compareTo(firstBig) == 1 && idBig.compareTo(lastBig) <= 0) {
				return true;
			}
		}

		if (firstBig.compareTo(lastBig) == 0
				&& (firstBig.compareTo(idBig) > 0 || firstBig.compareTo(idBig) < 0)) {
			return true;
		}

		return false;
	}

	public static boolean isBetween(GenericKey id, GenericKey first,
			GenericKey last) {
		ByteWrapper idBig = id.getHashKey();
		ByteWrapper firstBig = first.getHashKey();
		ByteWrapper lastBig = last.getHashKey();

		if (firstBig.compareTo(lastBig) == -1) {
			if (idBig.compareTo(firstBig) == 1
					&& idBig.compareTo(lastBig) == -1) {
				return true;
			}
		}

		if (firstBig.compareTo(lastBig) == 1) {
			if (idBig.compareTo(firstBig) == -1
					&& idBig.compareTo(lastBig) == -1) {
				return true;
			}
			if (idBig.compareTo(firstBig) == 1) {
				return true;
			}
		}

		return false;
	}

	public static boolean isBetweenNotify(GenericKey id, GenericKey first,
			GenericKey last) {
		ByteWrapper idBig = id.getHashKey();
		ByteWrapper firstBig = first.getHashKey();
		ByteWrapper lastBig = last.getHashKey();

		if (firstBig.compareTo(lastBig) == -1) {
			if (idBig.compareTo(firstBig) == 1
					&& idBig.compareTo(lastBig) == -1) {
				return true;
			}
		}

		if (firstBig.compareTo(lastBig) == 1) {
			if (idBig.compareTo(firstBig) == -1
					&& idBig.compareTo(lastBig) == -1) {
				return true;
			}
			if (idBig.compareTo(firstBig) == 1) {
				return true;
			}
		}

		if (firstBig.compareTo(lastBig) == 0) {
			return true;
		}

		return false;
	}
}