import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Inputs to test the correctness of the chord implementation.
 * 
 * @author thirunavukarasu
 *
 */
public class TestCasesClient {
	public static void main(String[] args) {
		if (args.length != 1) {
			throw new RuntimeException("Pass the node-0 hostname");
		}

		// Get default port.
		int port = 1099;
		// Get the main hostname.
		String mainNodeHostName = args[0];
		try {
		 Node masterNode = (Node) Naming.lookup("//" + mainNodeHostName + ":"
		 + port + "/" + "node00Node");
		
		 System.out.println("TestCase 1:");
		 System.out.println("Inserting a word and searching");
		 
		 
		 
//		node = (Node) Naming.lookup("//" + successor.getHost() + ":"
//					+ port + "/" + successor.getNodeNum() + "Node");
	
	} catch (MalformedURLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (RemoteException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (NotBoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
