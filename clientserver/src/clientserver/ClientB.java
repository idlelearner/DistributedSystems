package clientserver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class ClientB {
	protected String host, file;
	protected int port;
	protected DataInputStream in;
	protected DataOutputStream out;

	public static void main(String[] args) throws UnknownHostException,
			IOException, ClassNotFoundException, InterruptedException {
		InetAddress server = null;
		Socket sock = null;
		String host = args[0];
		int port = Integer.parseInt(args[1]);

		if (args.length != 2) {
			throw new RuntimeException("hostname and port number as arguments");
		}
		ClientB clt = new ClientB();
		System.out.println("Connecting to " + host + ":" + port + "..");

		Socket socket = new Socket(host, port);
		System.out.println("Connected.");

		OutputStream rawOut = socket.getOutputStream();
		InputStream rawIn = socket.getInputStream();
		ObjectOutputStream out = new ObjectOutputStream(rawOut);
		ObjectInputStream in = new ObjectInputStream(rawIn);

		int noOfAccts = 100;
		List<Integer> accts = clt.createAccts(noOfAccts, out, in);
		clt.deposit(accts, 100, out, in);
		System.out.println(clt.getTotalBalance(accts, out, in));

		int iterationCount = 5;
		int threadCount = 6;
		List<TransferClient> tcList = new ArrayList<TransferClient>();
		for (int i = 0; i < threadCount; i++) {
			TransferClient tc = new TransferClient(host, port, iterationCount,
					accts);
			tc.start();
			tcList.add(tc);
		}

		// Wait till all other threads finish
		for (int i = 0; i < threadCount; i++)
			tcList.get(i).join();

		System.out.println(clt.getTotalBalance(accts, out, in));

		Request exit = new Request();
		exit.transactionType = "exit";
		exit.params = new Parameter();
		out.writeObject(exit);
		in.close();
		socket.close();

	}

	public List<Integer> createAccts(int noOfAccts, ObjectOutputStream out,
			ObjectInputStream in) throws IOException, ClassNotFoundException {
		int i = 1;
		List<Integer> accts = new ArrayList<Integer>();
		while (i <= noOfAccts) {
			Request createAcct = new Request();
			createAcct.transactionType = "createAcct";
			createAcct.params = new Parameter("F" + i, "L" + i, "A" + i);
			out.writeObject(createAcct);
			String acct = (String) in.readObject();
			int acctID = Integer.parseInt(acct);
			accts.add(acctID);
			i++;
		}
		return accts;
	}

	public void deposit(List<Integer> accts, double amt,
			ObjectOutputStream out, ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		for (int acct : accts) {
			Request deposit = new Request();
			deposit.transactionType = "deposit";
			deposit.params = new Parameter(acct, amt);
			out.writeObject(deposit);
			String status = (String) in.readObject();
			// System.out.println(status);
		}
	}

	public double getTotalBalance(List<Integer> accts, ObjectOutputStream out,
			ObjectInputStream in) throws IOException, ClassNotFoundException {
		Double total = 0.0;
		for (int acct : accts) {
			Request getBalance = new Request();
			getBalance.transactionType = "getBalance";
			getBalance.params = new Parameter(acct);
			out.writeObject(getBalance);
			String s = (String) in.readObject();
			total = total + Double.parseDouble(s);
		}
		return total;
	}
}
