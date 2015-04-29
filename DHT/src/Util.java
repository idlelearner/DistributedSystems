import java.io.IOException;
import java.net.InetAddress;

/**
 * Class containing the utility methods.
 * 
 * @author thirunavukarasu
 *
 */
public class Util {
	public static String getHosttName() throws IOException
    {
		return InetAddress.getLocalHost().getHostName();
    }
}
