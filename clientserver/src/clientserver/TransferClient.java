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

public class TransferClient extends Thread {
	String host;
	int port;
	Socket socket;
	int iterationCount;
	List<Integer> accts = new ArrayList<Integer>();

	public TransferClient(String host, int port, int itCount,
			List<Integer> accts) {
		this.host = host;
		this.port = port;
		this.iterationCount = itCount;
		this.accts = accts;
		try {
			socket = new Socket(host, port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			OutputStream rawOut = socket.getOutputStream();
			InputStream rawIn = socket.getInputStream();
			ObjectOutputStream out = new ObjectOutputStream(rawOut);
			ObjectInputStream in = new ObjectInputStream(rawIn);
			Random rnd = new Random();
			for (int i = 0; i < iterationCount; i++) {
				// Get random accts and perform transfer.
				int rndacctID1 = rnd.nextInt(accts.size());
				int rndacctID2 = rnd.nextInt(accts.size());
				transfer(accts.get(rndacctID1), accts.get(rndacctID2), 100,
						out, in);
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

	public void transfer(int acctID1, int acctID2, double amt,
			ObjectOutputStream out, ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		Request transfer = new Request();
		transfer.transactionType = "transfer";
		transfer.params = new Parameter(acctID1, acctID2, 100.0);
		out.writeObject(transfer);
		String status = (String) in.readObject();
		System.out.println(status);

		// TODO Log to file
	}
}
