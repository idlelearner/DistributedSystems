import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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
			Node node = (Node) Naming.lookup("//" + mainNodeHostName + ":"
					+ port + "/" + "node00Node");
			int j = 0;
			while (true) {
				Node curNode = node.getNodeDetails();
				System.out.println("Printing out current node " + j
						+ " details");
				System.out.println("Node name : " + curNode.getNodeID());
				System.out.println("Finger Table Entries : ");
				for (int i = 0; i < curNode.getFingerTable().size(); i++) {
					System.out.println("Finger table entry " + i + " "
							+ curNode.getFingerTable().get(i));
				}
				System.out.println("Number of words in this node : "
						+ curNode.getCountOfWordEntriesInMap());
				
				System.out.println("Arrangement of words : ");
				Iterator<Map.Entry<String, Set<WordEntry>>> it = curNode
						.getWordEntryMap().entrySet().iterator();
				Map.Entry<String, Set<WordEntry>> entry;
				while (it.hasNext()) {
					entry = it.next();
					System.out.println(entry.getKey() + " : " + entry.getValue());
					System.out.println("------------------------------------------");
				}
				NodeKey successor = curNode.getSuccessor();
				System.out.println("Successor : " + successor);
				if (successor.getNodeNum().equals("node00"))
					break;
				node = (Node) Naming.lookup("//" + successor.getHost() + ":"
						+ port + "/" + successor.getNodeNum() + "Node");
				j++;
			}

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
}
