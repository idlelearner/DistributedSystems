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
	protected ClientLogger log = ClientLogger.getInstance();

	public static void main(String[] args) throws UnknownHostException,
			IOException, ClassNotFoundException, InterruptedException {
		InetAddress server = null;
		Socket sock = null;
		String host = args[0];
		int port = Integer.parseInt(args[1]);
		int iterationCount = Integer.parseInt(args[2]);
		int threadCount = Integer.parseInt(args[3]);
		if (args.length < 4) {
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
		System.out.println("Creating accts...");
		List<Integer> accts = clt.createAccts(noOfAccts, out, in);
		System.out.println("Depositing amt in accts...");
		clt.deposit(accts, 100, out, in);
		System.out.println("Balance before transferring amt between accts : "
				+ clt.getTotalBalance(accts, out, in));
		// Create threads to transfer amount
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

		System.out.println("Balance after transferring amt between accts : "
				+ clt.getTotalBalance(accts, out, in));

		Request exit = new Request();
		exit.transactionType = "final client exit";
		exit.params = new Parameter();
		clt.log.write("\nfinal client exit");
		out.writeObject(exit);
		in.close();
		socket.close();

	}

	/**
	 * Function create accts
	 * 
	 * @param noOfAccts
	 * @param out
	 * @param in
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public List<Integer> createAccts(int noOfAccts, ObjectOutputStream out,
			ObjectInputStream in) throws IOException, ClassNotFoundException {
		int i = 1;
		List<Integer> accts = new ArrayList<Integer>();
		while (i <= noOfAccts) {
			Request createAcct = new Request();
			createAcct.transactionType = "createAcct";
			createAcct.params = new Parameter("\nF" + i, "L" + i, "A" + i);
			out.writeObject(createAcct);
			String acct = (String) in.readObject();
			int acctID = Integer.parseInt(acct);
			accts.add(acctID);
			i++;
		}
		return accts;
	}

	/**
	 * Deposit amount
	 * 
	 * @param accts
	 * @param amt
	 * @param out
	 * @param in
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void deposit(List<Integer> accts, int amt, ObjectOutputStream out,
			ObjectInputStream in) throws IOException, ClassNotFoundException {
		for (int acct : accts) {
			Request deposit = new Request();
			deposit.transactionType = "deposit";
			deposit.params = new Parameter(acct, amt);
			out.writeObject(deposit);
			String status = (String) in.readObject();
		}
	}

	/**
	 * Get balance for acct
	 * 
	 * @param accts
	 * @param out
	 * @param in
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public int getTotalBalance(List<Integer> accts, ObjectOutputStream out,
			ObjectInputStream in) throws IOException, ClassNotFoundException {
		int total = 0;
		for (int acct : accts) {
			Request getBalance = new Request();
			getBalance.transactionType = "getBalance";
			getBalance.params = new Parameter(acct);
			out.writeObject(getBalance);
			String s = (String) in.readObject();
			total = total + Integer.parseInt(s);
		}
		return total;
	}
}
