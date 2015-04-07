import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Class to handle server requests from other servers
 * 
 * @author thirunavukarasu
 *
 */
public class PeerServerConnectionHandler extends Thread {
	private ServerSocket serverForPeerConnections;
	private ServerManager serverManager;
	protected ServerLogger log;

	public PeerServerConnectionHandler(ServerSocket serverForPeerConnections,
			ServerManager serverManager) {
		this.serverForPeerConnections = serverForPeerConnections;
		this.serverManager = serverManager;
		log = ServerLogger.getInstance();
	}

	public void run() {
		while (true) {
			System.out.println("Waiting for a client request");
			Socket peerServer;
			try {
				peerServer = serverForPeerConnections.accept();
				System.out.println("Received request from "
						+ peerServer.getInetAddress());
				PeerServerRequestHandler s = new PeerServerRequestHandler(
						peerServer, serverManager);
				s.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
