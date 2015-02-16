package clientserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;

public class Server extends Thread {

	protected Socket s;
	protected BankOperations bankOperations;

	public Server(Socket s, BankOperations bankOperations) {
		System.out.println("New client.");
		this.s = s;
		this.bankOperations = bankOperations;
	}

	public static void main(String[] args) throws IOException {

		if (args.length != 1)
			throw new RuntimeException("Syntax: EchoServer port-number");

		System.out.println("Starting on port " + args[0]);
		ServerSocket server = new ServerSocket(Integer.parseInt(args[0]));
		BankOperations bankOperations = new BankOperations();
		while (true) {
			System.out.println("Waiting for a client request");
			Socket client = server.accept();
			System.out.println("Received request from "
					+ client.getInetAddress());
			Server s = new Server(client, bankOperations);
			s.start();
		}
	}

	public void run() {
		try {
			InputStream istream = s.getInputStream();
			OutputStream ostream = s.getOutputStream();
			// new PrintStream (ostream).println
			// ("Welcome to the multithreaded echo server.");
			ObjectInputStream in = new ObjectInputStream(istream);
			ObjectOutputStream out = new ObjectOutputStream(ostream);
			try {
				StringBuilder response = new StringBuilder();
				while (true) {
					Request r = (Request) in.readObject();
					System.out.println("Server Transaction type : "
							+ r.getTransactionType());
					if (r.getTransactionType().equals("exit"))
						break;
					// response.append(performOperation(r) + "\n");
					out.writeObject(performOperation(r));
				}
				// out.writeObject(response.toString());
				out.writeObject("exit");
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				// Can exit for other transactions as well
				e.printStackTrace();
			}
			in.close();
			// while ((read = istream.read(buffer)) >= 0) {
			// ostream.write(buffer, 0, read);
			// System.out.write(buffer, 0, read);
			// // System.out.flush();
			// }

			out.close();
			System.out.println("Client exit.");
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				s.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	public String performOperation(Request request) {
		StringBuilder status = new StringBuilder();
		Parameter param = request.getParams();
		switch (request.getTransactionType()) {
		case "createAcct":
			status.append(bankOperations.createAccount(param.getFirstname(),
					param.getLastname(), param.getAddress()));
			break;
		case "deposit":
			status.append(bankOperations.deposit(param.getAcctID(),
					param.getAmt()));
			break;
		case "withdraw":
			status.append(bankOperations.withdraw(param.getAcctID(),
					param.getAmt()));
			break;

		case "getBalance":
			status.append(bankOperations.getBalance(param.getAcctID()));
			break;
		case "transferAmount":
			status.append(bankOperations.transfer(param.getSrcAcctID(),
					param.getDestAcctID(), param.getAmt()));
			break;
		default:
			status.append("Operation not supported!");
			break;
		}
		return status.toString();
	}
}
