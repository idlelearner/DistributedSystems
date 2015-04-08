import java.io.IOException;
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

	public PeerServerConnectionHandler(ServerSocket serverForPeerConnections,
			ServerManager serverManager) {
		this.serverForPeerConnections = serverForPeerConnections;
		this.serverManager = serverManager;
	}

	public void run() {
		System.out.println("PeerConnection handler started : "
				+ serverForPeerConnections.getLocalPort());
		while (true) {
			System.out.println("Waiting for a peer server request");
			Socket peerServer;
			try {
				peerServer = serverForPeerConnections.accept();
				System.out.println("Received server request from "
						+ peerServer.getInetAddress() + " : "
						+ peerServer.getPort());
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
