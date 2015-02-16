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

		System.out.println("Connecting to " + host + ":" + port + "..");

		Socket socket = new Socket(host, port);
		System.out.println("Connected.");

		OutputStream rawOut = socket.getOutputStream();
		InputStream rawIn = socket.getInputStream();
		ObjectOutputStream out = new ObjectOutputStream(rawOut);
		ObjectInputStream in = new ObjectInputStream(rawIn);
		// Write the object
		Request r = new Request();
		r.transactionType = "createAcct";
		r.params = new Parameter();
		out.writeObject(r);
		r = new Request();
		r.transactionType = "createAcct";
		r.params = new Parameter();
		out.writeObject(r);
		Request deposit1 = new Request();
		deposit1.transactionType = "deposit";
		deposit1.params = new Parameter();
		out.writeObject(deposit1);
		Request r1 = new Request();
		r1.transactionType = "exit";
		r1.params = new Parameter();
		out.writeObject(r1);
		Request r2 = new Request();
		r1.transactionType = "exit";
		r1.params = new Parameter();
		out.writeObject(r1);
		Request r3 = new Request();
		r1.transactionType = "exit";
		r1.params = new Parameter();
		out.writeObject(r1);
		while (true) {
			System.out.println("reading");
			String s = (String) in.readObject();
			System.out.println(s);
			if (s.equals("exit")) {
				in.close();
				socket.close();
				break;
			}
		}

	}

}
