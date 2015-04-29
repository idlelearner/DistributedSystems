import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.registry.Registry;
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

		if (args.length != 1) {
			throw new RuntimeException(
					"Pass the word meaning file as argument!");
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
		String hostname = args[0] + ":" + port;
		Node mainNode = (Node) Naming.lookup("//" + hostname + ":" + port + "/"
				+ "node00");

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

		// If the word entered quit, else fetch the work
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
			System.out.println("Searched : " + entry.getWord() + " : "
					+ entry.getMeaning());

		}

		// write a loop and which searches and returns.
		// Then search for the word

		// create RMI registry

		Node bootstrapNode;
		// locate the booststrapping node, the node-0
		// if(bootstrapNode == null) {
		// //this is the first node being added
		//
		// //bind the service to the registry
		// //TODO : How to do this
		// try {
		// Naming.bind();
		// }catch (RemoteException e) {
		// //log this in the client file
		// }
		//
		// //create the bootstrap node
		//
		// //do what now ?
		// }else {
		// //existing node, so let us just allow this node to join the chord
		// ring
		//
		// //TODO : complete this
		// }
	}

}
