import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Command line to print the Chord details
 * 
 * @author thirunavukarasu
 *
 */
public class GetRingDetailsClient {
	public static void main(String[] args) {
		if (args.length != 1) {
			throw new RuntimeException("Pass the node-0 hostname");
		}

		// Get default port.
		int port = 1099;
		// Get the main hostname.
		String mainNodeHostName = args[0];
		try {
			Node mainNode = (Node) Naming.lookup("//" + mainNodeHostName + ":"
					+ port + "/" + "node00Node");

			// mainNode should return its nodeDetails
			// while(Get successor != node00)
			// print Get nodedetails of successor.
			// print node details.
			mainNode.printChordRingInfo();

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
