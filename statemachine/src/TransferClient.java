import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This thread connects to one of the server and performs transfer operations
 * 
 * @author dhass
 *
 */
public class TransferClient extends Thread {
	String host;
	int port;
	Socket socket;
	int iterationCount;
	List<Integer> accts = new ArrayList<Integer>();
	ClientLogger log;

	public TransferClient(String host, int port) {
		this.host = host;
		this.port = port;
		this.iterationCount = 1;
		// Initialize acct IDs.
		for (int i = 1; i <= 10; i++)
			accts.add(i);
		log = ClientLogger.getInstance();
		try {
			socket = new Socket(host, port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			System.out.println("\nNew thread created :"
					+ Thread.currentThread().getId());
			OutputStream rawOut = socket.getOutputStream();
			InputStream rawIn = socket.getInputStream();
			ObjectOutputStream out = new ObjectOutputStream(rawOut);
			ObjectInputStream in = new ObjectInputStream(rawIn);
			Random rnd = new Random();
			for (int i = 0; i < iterationCount; i++) {
				// Get random accts and perform transfer.
				System.out.println("\nThread ID :"
						+ Thread.currentThread().getId() + " Iteration " + i);
				int rndacctID1 = rnd.nextInt(accts.size());
				int rndacctID2 = rnd.nextInt(accts.size());
				transfer(accts.get(rndacctID1), accts.get(rndacctID2), 10, out,
						in);
				System.out.println("\nTransfer completed between "
						+ accts.get(rndacctID1) + " and "
						+ accts.get(rndacctID2));
			}

			// Send a halt message and to shutdown the server.
			ClientRequest exit = new ClientRequest();
			exit.transactionType = "exit";
			exit.params = new Parameter();
			out.writeObject(exit);

			in.close();
			socket.close();

		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Transfer function calling the server
	 * 
	 * @param acctID1
	 * @param acctID2
	 * @param amt
	 * @param out
	 * @param in
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void transfer(int acctID1, int acctID2, int amt,
			ObjectOutputStream out, ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		ClientRequest transfer = new ClientRequest();
		transfer.transactionType = "transfer";
		transfer.params = new Parameter(acctID1, acctID2, amt);
		System.out.println("\nclientrequest type" + transfer.transactionType);
		System.out.println("\nclient params" + transfer.params);
		out.writeObject(transfer);
		String status = (String) in.readObject();
		System.out.println("\nServer Response" + status);
	}
}
