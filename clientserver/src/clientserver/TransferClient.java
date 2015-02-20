package clientserver;

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
 * This client loops till iteration count and transfers amount between randomly
 * selected accounts
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

	public TransferClient(String host, int port, int itCount,
			List<Integer> accts) {
		this.host = host;
		this.port = port;
		this.iterationCount = itCount;
		this.accts = accts;
		log = ClientLogger.getInstance();
		try {
			socket = new Socket(host, port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			log.write("\nNew thread created :" + Thread.currentThread().getId());
			OutputStream rawOut = socket.getOutputStream();
			InputStream rawIn = socket.getInputStream();
			ObjectOutputStream out = new ObjectOutputStream(rawOut);
			ObjectInputStream in = new ObjectInputStream(rawIn);
			Random rnd = new Random();
			for (int i = 0; i < iterationCount; i++) {
				// Get random accts and perform transfer.
				log.write("\nThread ID :" + Thread.currentThread().getId()
						+ " Iteration " + i);
				int rndacctID1 = rnd.nextInt(accts.size());
				int rndacctID2 = rnd.nextInt(accts.size());
				transfer(accts.get(rndacctID1), accts.get(rndacctID2), 10, out,
						in);
				log.write("\nTransfer completed between "
						+ accts.get(rndacctID1) + " and "
						+ accts.get(rndacctID2));
			}
			Request exit = new Request();
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
		Request transfer = new Request();
		transfer.transactionType = "transfer";
		transfer.params = new Parameter(acctID1, acctID2, amt);
		log.write("\nclientrequest type" + transfer.transactionType);
		log.write("\nclient params" + transfer.params);
		out.writeObject(transfer);
		String status = (String) in.readObject();
		log.write("\nServer Response" + status);
	}
}
