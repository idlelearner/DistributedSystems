package clientserver;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientA {

	protected String host, file;
	protected int port;
	protected DataInputStream in;
	protected DataOutputStream out;

	public static void main(String[] args) throws UnknownHostException,
			IOException {
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
		// Write the object
		Request r = new Request();
		r.transactionType = "DepositTest";

		out.writeObject(r);

		// BufferedReader buffreader = new BufferedReader(new InputStreamReader(
		// rawIn));
		// PrintWriter printer = new PrintWriter(new
		// OutputStreamWriter(rawOut));
		//
		// BufferedReader keyboard = new BufferedReader(new InputStreamReader(
		// System.in));

		String line = "Hello!!";
		// printer.println(line);
		// printer.flush();

		System.out.println(r.transactionType);
	}

}
