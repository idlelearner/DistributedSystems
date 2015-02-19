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

import com.sun.org.apache.bcel.internal.generic.InstructionConstants.Clinit;

public class ClientA {

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
		ClientA clt = new ClientA();
		System.out.println("Connecting to " + host + ":" + port + "..");

		Socket socket = new Socket(host, port);
		System.out.println("Connected.");

		OutputStream rawOut = socket.getOutputStream();
		InputStream rawIn = socket.getInputStream();
		ObjectOutputStream out = new ObjectOutputStream(rawOut);
		ObjectInputStream in = new ObjectInputStream(rawIn);

		int noOfAccts = 2;
		List<Integer> accts = clt.createAccts(noOfAccts, out, in);
		int acctID1 = accts.get(0);
		int acctID2 = accts.get(1);

		int amt = 100;
		// Deposit
		clt.deposit(accts, 100, out, in);

		// Get balance
		clt.getBalance(accts, out, in);

		// Transfer amount
		clt.transfer(acctID1, acctID2, amt, out, in);

		// Get balance
		clt.getBalance(accts, out, in);

		// Withdraw amount.
		clt.withdraw(accts, amt, out, in);

		// Transfer amount
		clt.transfer(acctID1, acctID2, amt, out, in);

		// Get balance
		clt.getBalance(accts, out, in);

		Request exit = new Request();
		exit.transactionType = "final client exit";
		exit.params = new Parameter();
		out.writeObject(exit);
		in.close();
		socket.close();

	}

	/**
	 * Client function to create acct
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
			createAcct.params = new Parameter("F" + i, "L" + i, "A" + i);
			out.writeObject(createAcct);
			String acct = (String) in.readObject();
			int acctID = Integer.parseInt(acct);
			accts.add(acctID);
			i++;
		}
		return accts;
	}

	/**
	 * Client function to get balance for an acct
	 * 
	 * @param accts
	 * @param out
	 * @param in
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void getBalance(List<Integer> accts, ObjectOutputStream out,
			ObjectInputStream in) throws IOException, ClassNotFoundException {
		for (int acct : accts) {
			Request getBalance = new Request();
			getBalance.transactionType = "getBalance";
			getBalance.params = new Parameter(acct);
			out.writeObject(getBalance);
			String status = (String) in.readObject();
			System.out.println(status);
		}
	}

	/**
	 * client function to deposit amount to an acct.
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
			System.out.println(status);
		}
	}

	/**
	 * Client function to withdraw amount from acct.
	 * 
	 * @param accts
	 * @param amt
	 * @param out
	 * @param in
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void withdraw(List<Integer> accts, int amt, ObjectOutputStream out,
			ObjectInputStream in) throws IOException, ClassNotFoundException {
		for (int acct : accts) {
			Request withdraw = new Request();
			withdraw.transactionType = "withdraw";
			withdraw.params = new Parameter(acct, amt);
			out.writeObject(withdraw);
			String status = (String) in.readObject();
			System.out.println(status);
		}
	}

	/**
	 * Client Function to transfer amt from source to destination acct
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
		transfer.params = new Parameter(acctID1, acctID2, 100);
		out.writeObject(transfer);
		String status = (String) in.readObject();
		System.out.println(status);
	}

}
