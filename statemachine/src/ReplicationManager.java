import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReplicationManager {
	List<ServerDetails> peerServerDetails;
	Map<Integer, Socket> peerServerSocketMap;

	public ReplicationManager() {
		peerServerDetails = new ArrayList<ServerDetails>();

	}

	public ReplicationManager(List<ServerDetails> peerServers) {
		peerServerDetails = peerServers;
		// Establish Connection between peer servers.
		startConnectionWithPeerServers();
	}

	/**
	 * Establish connections with the peer servers to maintain
	 */
	public void startConnectionWithPeerServers() {
		for (ServerDetails peerServer : peerServerDetails) {
			Socket socket;
			try {
				socket = new Socket(peerServer.getServerHostName(),
						peerServer.getPeerServerPort());
				peerServerSocketMap.put(peerServer.getID(), socket);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public void multiCastMessage(Request req) {
		req.setReqType("New");
		for (int peerServerID : peerServerSocketMap.keySet()) {
			Socket s = peerServerSocketMap.get(peerServerID);
			sendMessage(s, req);
		}
	}

	/**
	 * Multicast acknowledgements to the peerservers.
	 * 
	 * @param req
	 */
	public void multicastAcknowledgements(Request req) {
		// Specify that the request is acknowledgement
		req.setReqType("Ack");
		for (int peerServerID : peerServerSocketMap.keySet()) {
			Socket s = peerServerSocketMap.get(peerServerID);
			sendMessage(s, req);
		}
	}

	public void sendMessage(Socket socket, Request req) {

		try {
			OutputStream rawOut = socket.getOutputStream();
			InputStream rawIn = socket.getInputStream();
			ObjectOutputStream out = new ObjectOutputStream(rawOut);
			ObjectInputStream in = new ObjectInputStream(rawIn);

			out.writeObject(req);
			// String status = (String) in.readObject();
			// System.out.println(status);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	// public void receiveMessages();

}
