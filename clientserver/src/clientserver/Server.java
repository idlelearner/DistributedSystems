package clientserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;

public class Server extends Thread {
	Hashtable<Integer, Account> accountMap = new Hashtable<Integer, Account>();
	volatile int maxAcctID;
	protected Socket s;

	public Server(Socket s) {
		System.out.println("New client.");
		this.s = s;
	}

	public static void main(String[] args) throws IOException {

		if (args.length != 1)
			throw new RuntimeException("Syntax: EchoServer port-number");

		System.out.println("Starting on port " + args[0]);
		ServerSocket server = new ServerSocket(Integer.parseInt(args[0]));

		while (true) {
			System.out.println("Waiting for a client request");
			Socket client = server.accept();
			System.out.println("Received request from "
					+ client.getInetAddress());
			Server s = new Server(client);
			s.start();
		}
	}

	public void run() {
		try {
			InputStream istream = s.getInputStream();
			OutputStream ostream = s.getOutputStream();
			// new PrintStream (ostream).println
			// ("Welcome to the multithreaded echo server.");
			byte buffer[] = new byte[16];
			int read;
			ObjectInputStream in = new ObjectInputStream(istream);
			try {
				Request r = (Request) in.readObject();
				System.out.println("Server Transaction type : "
						+ r.getTransactionType());
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			in.close();
			// while ((read = istream.read(buffer)) >= 0) {
			// ostream.write(buffer, 0, read);
			// System.out.write(buffer, 0, read);
			// // System.out.flush();
			// }

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

	public Account createAccount() {
		return new Account(0, 0.0);
	}
}
