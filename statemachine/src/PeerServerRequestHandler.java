import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Class to handle server requests from other servers
 * 
 * @author thirunavukarasu
 *
 */
public class PeerServerRequestHandler extends Thread {
	private Socket s;
	private ServerManager serverManager;
	protected ServerLogger log;

	public PeerServerRequestHandler(Socket s, ServerManager serverManager) {
		this.s = s;
		this.serverManager = serverManager;
		log = ServerLogger.getInstance();
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
				while (true) {
					// Get the request the from the server and perform some
					// operation like give acknowledgement.
					// TODO : get request and reply acknowledgement.
					Request r = (Request) in.readObject();
					// log.write("Transaction type : " +
					// r.getTransactionType());
					// log.write("Parameter received : " + r.params);
					if (r.getTransactionType().contains("exit"))
						break;
					// response.append(performOperation(r) + "\n");
					// out.writeObject(performOperation(r));
					out.writeObject("return response to client after performing operation");
				}
				// out.writeObject(response.toString());
				out.writeObject("exit");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			in.close();

			out.close();
//			log.write("Server connection exitting.");
//			System.out.println("Server exitting.");
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

}
