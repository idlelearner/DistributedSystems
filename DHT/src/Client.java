import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Client class for parsing generating dictionary and communicating with chord
 * ring rmi calls made from the client
 * 
 * @author varun
 *
 */
public class Client {

	public static void main(String[] args) throws IOException,
			NotBoundException {

		if (args.length != 2) {
			throw new RuntimeException(
					"Pass the word meaning file and node-0 host as argument!");
		}

		HashMap<String, String> wordMeaningMap = new HashMap<String, String>();
		// parse the word dictionary to create a local map
		// Read the file and populate in a hashmap to insert in the DHT
		String wordFile = args[0];
		File file = new File(wordFile);
		BufferedReader buffer = new BufferedReader(new FileReader(file));
		String line;
		while ((line = buffer.readLine()) != null) {
			line = line.trim();
			// Split the line eg - another : one more, different
			String wordParts[] = line.split(":");
			wordMeaningMap.put(wordParts[0].trim(), wordParts[1].trim());
		}

		System.out.println("Print the hashmap...............");
		for (String key : wordMeaningMap.keySet())
			System.out.println(key + " : " + wordMeaningMap.get(key));

		// generate a node
		// Bind to the master node.

		// lookup for the node in the master node.
		// What should be the master node URL and class
		System.setSecurityManager(new RMISecurityManager());
		// default port
		int port = 1099;
		String hostname = args[1] + ":" + port;
		Node mainNode = (Node) Naming.lookup("//" + hostname + "/"
				+ "node00Node");

		// get the node to insert
		// insert.
		for (String key : wordMeaningMap.keySet()) {
			// Find the node where to insert.
			NodeKey nodeToBeInserted = mainNode.find_node(key);
			Node curNodeToInsert = (Node) Naming.lookup("//"
					+ nodeToBeInserted.getHost() + ":" + port + "/"
					+ nodeToBeInserted.getNodeNum() + "Node");

			// Insert in that word.
			curNodeToInsert.insert(key, wordMeaningMap.get(key));
			System.out.println("Inserted : " + key + " : "
					+ wordMeaningMap.get(key));
		}

		// If the word entered quit, else fetch the word
		String word = "";
		while (true) {
			Scanner in = new Scanner(System.in);
			System.out
					.print("Enter the word to get the meaning , enter exit to quit : ");
			word = in.next();
			if (word.equals("exit"))
				break;
			// Hash the word to get the key
			NodeKey nodeToBeSearched = mainNode.find_node(word);
			Node curNodeToSearch = (Node) Naming.lookup("//"
					+ nodeToBeSearched.getHost() + ":" + port + "/"
					+ nodeToBeSearched.getNodeNum() + "Node");

			// Insert in that word.
			WordEntry entry = curNodeToSearch.lookup(word);
			if (entry == null) {
				System.out.println("Word : " + word + " is not in dictionary");
			} else
				System.out.println("Searched and found: " + entry.getWord()
						+ " : " + entry.getMeaning());

		}
	}

}
