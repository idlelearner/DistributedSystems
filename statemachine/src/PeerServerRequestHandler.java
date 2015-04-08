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
		InputStream istream = null;
		OutputStream ostream = null;
		ObjectInputStream in = null;
		ObjectOutputStream out = null;
		try {
			istream = s.getInputStream();
			ostream = s.getOutputStream();
			in = new ObjectInputStream(istream);
			out = new ObjectOutputStream(ostream);

			while (true) {
				try {
					// Get the request the from the peer servers
					// synchronized (in) {
					Request r = (Request) in.readObject();
					serverManager.receiveRequest(r);
					//TODO : if halt stop this thread.
					// }
					// in.reset();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			// log.write("Server connection exitting.");
			// System.out.println("Server exitting.");
		} catch (IOException ex) {
			ex.printStackTrace();
			try {
				in.close();
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} finally {
			try {
				s.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}
