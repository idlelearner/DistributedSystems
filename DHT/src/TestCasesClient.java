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

	String mainNodeHostName = "";
	// Get default port.
	int port = 1099;

	public TestCasesClient(String hostName) {
		mainNodeHostName = hostName;
	}

	public static void main(String[] args) {
		if (args.length != 1) {
			throw new RuntimeException("Pass the node-0 hostname");
		}
		String mainNodeHostName = args[0];
		TestCasesClient t = new TestCasesClient(mainNodeHostName);

		System.out.println("Executing TestCase 1:");
		System.out.println("Inserting a word and searching");

		System.out.println("Inserting a word : Abacus");
		t.insert("Abacus",
				"To see or use an abacus in your dream refers to your outdated views");

		System.out.print("Searching word : Abacus");
		t.search("Abacus");

		System.out.println("Executing TestCase 1: Executed");
		System.out
				.println("// ///////////////////////////////////////////////////////////////////");

		System.out.println("Executing TestCase 2:");
		System.out.println("Searching a word that is not in dictionary");

		System.out.print("Searching word : abc");
		t.search("abc");

		System.out.println("Executing TestCase 2: Executed");

		System.out
				.println("// ///////////////////////////////////////////////////////////////////");

		System.out.println("Executing TestCase 3:");
		System.out
				.println("Inserting word in the dictionary and finding the word count");
		System.out
				.println("No. of words in dictionary : " + t.totalNoOfWords());
		System.out.println("Inserting a word : duffin");
		t.insert("duffin",
				"a sweet cake which is a combination of a muffin and a doughnut");
		System.out
				.println("No. of words in dictionary : " + t.totalNoOfWords());

		System.out.println("Executing TestCase 3: Executed");
		System.out
				.println("// ///////////////////////////////////////////////////////////////////");
	}

	public void insert(String word, String meaning) {
		Node mainNode;
		try {
			mainNode = (Node) Naming.lookup("//" + mainNodeHostName + ":"
					+ port + "/" + "node00Node");

			NodeKey nodeToBeInserted = mainNode.find_node(word);
			Node curNodeToInsert = (Node) Naming.lookup("//"
					+ nodeToBeInserted.getHost() + ":" + port + "/"
					+ nodeToBeInserted.getNodeNum() + "Node");

			// Insert in that word.
			curNodeToInsert.insert(word, meaning);
			System.out.println("Inserted : " + word + " : " + meaning);
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

	public void search(String word) {
		Node mainNode;

		try {
			mainNode = (Node) Naming.lookup("//" + mainNodeHostName + ":"
					+ port + "/" + "node00Node");

			NodeKey nodeToBeSearched = mainNode.find_node(word);
			Node curNodeToSearch = (Node) Naming.lookup("//"
					+ nodeToBeSearched.getHost() + ":" + port + "/"
					+ nodeToBeSearched.getNodeNum() + "Node");

			// Insert in that word.
			WordEntry entry = curNodeToSearch.lookup(word);
			if (entry == null) {
				System.out.println("Word : " + word + " is not in dictionary");
			} else
				System.out.println("Searched and found: " + entry.getWord() + " : "
						+ entry.getMeaning());
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

	public int totalNoOfWords() {
		int count = 0;
		Node node;
		try {
			node = (Node) Naming.lookup("//" + mainNodeHostName + ":" + port
					+ "/" + "node00Node");

			while (true) {
				Node curNode = node.getNodeDetails();
				count += curNode.getCountOfWordEntriesInMap();
				NodeKey successor = curNode.getSuccessor();
				if (successor.getNodeNum().equals("node00"))
					break;
				node = (Node) Naming.lookup("//" + successor.getHost() + ":"
						+ port + "/" + successor.getNodeNum() + "Node");
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
		return count;
	}
}
