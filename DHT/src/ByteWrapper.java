import java.io.Serializable;

public class ByteWrapper implements Serializable {
	private final byte[] number;

	//private final BigInt upperBound = powerOfTwo(160);

	public ByteWrapper(byte[] number)
	{
		this.number = number;
	}

	public String toHexString()
	{
		String hexString = "";
		String t;
		for (byte b : number)
		{
			t = Integer.toHexString(b & 0xff).toUpperCase();

			if (t.length() == 1)
			{
				t = "0" + t;
			}

			hexString += t;
		}

		return hexString;
	}

	public int compareTo(ByteWrapper big)
	{
		byte[] array = big.getBytes();
		int i;
		int lenLoc = this.number.length;
		int lenRem = big.getBytes().length;
		byte[] temp;

		if (lenLoc > lenRem)
		{
			temp = new byte[lenLoc];

			System.arraycopy(array, 0, temp, lenLoc - lenRem, lenRem);

			for (i = 0; i < lenRem - lenLoc; i++)
			{
				temp[i] = 0;
			}

			for (i = 0; i < lenLoc; i++)
			{
				if ((this.number[i]& 0xff) > (temp[i]& 0xff))
					return 1;
				else if ((this.number[i]&0xff) < (temp[i]&0xff))
					return -1;
				else
					continue;
			}
			return 0;
		}
		else if (lenRem > lenLoc)
		{
			temp = new byte[lenRem];

			System.arraycopy(this.number, 0, temp, lenRem - lenLoc, lenLoc);

			for (i = 0;  i < lenLoc - lenRem; i++)
			{
				temp[i] = 0;
			}

			for (i = 0; i < lenRem; i++)
			{
				if ((temp[i]& 0xff) > (array[i]& 0xff))
					return 1;
				else if ((temp[i]&0xff) < (array[i]&0xff))
					return -1;
				else
					continue;
			}
			return 0;
		}
		else
		{

			for (i = 0; i < lenLoc; i++)
			{

				if ((this.number[i]& 0xff ) > (array[i]& 0xff ))
					return 1;
				else if ((this.number[i]& 0xff ) < (array[i]& 0xff ))
					return -1;
				else
					continue;
			}

			return 0;
		}
	}

	public byte[] getBytes()
	{
		byte[] nBigNum = new byte[this.number.length];

		System.arraycopy(this.number, 0, nBigNum, 0, this.number.length);

		return nBigNum;
	}

	public boolean equals(ByteWrapper b)
	{
		return (compareTo(b) == 0);
	}

	public boolean equals(byte[] b)
	{
		return (compareTo(new ByteWrapper(b)) == 0);
	}
}