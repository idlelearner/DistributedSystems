import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Replication Manager maintains connections with the peer servers and
 * multicasts the messages and acknowledgements
 * 
 * @author dhass
 *
 */
public class ReplicationManager {
	List<ServerDetails> peerServerDetails;
	Map<Integer, Socket> peerServerSocketMap = new HashMap<Integer, Socket>();
	Map<Integer, ObjectOutputStream> peerServerOuputStreamMap = new HashMap<>();

	public ReplicationManager() {
		peerServerDetails = new ArrayList<ServerDetails>();
	}

	public ReplicationManager(List<ServerDetails> peerServers) {
		peerServerDetails = peerServers;
		// Establish Connection between peer servers.
	}

	/**
	 * Establish connections with the peer servers to maintain connections
	 */
	public void startConnectionWithPeerServers() {
		try {
			System.out.println("Replication manager init : starting");
			Thread.sleep(5000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		for (ServerDetails peerServer : peerServerDetails) {
			try {
				System.out.println("Establishing connection with "
						+ peerServer.getServerHostName() + " : "
						+ peerServer.getPeerServerPort());
				Socket socket = new Socket(peerServer.getServerHostName(),
						peerServer.getPeerServerPort());

				peerServerSocketMap.put(peerServer.getID(), socket);
				OutputStream rawOut = socket.getOutputStream();
				ObjectOutputStream out = new ObjectOutputStream(rawOut);
				peerServerOuputStreamMap.put(peerServer.getID(), out);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Established connections with 2 other peer servers");

	}

	/**
	 * Multicast the new requests to all the peer servers.
	 * 
	 * @param req
	 */
	public void multiCastMessage(Request req) {
		req.setReqType("New");
		for (int peerServerID : peerServerSocketMap.keySet()) {
			Socket s = peerServerSocketMap.get(peerServerID);
			// sendMessage(s, req);
			sendMessage(peerServerID, req);
		}
	}

	/**
	 * Multicast acknowledgements to the peer servers.
	 * 
	 * @param req
	 */
	public void multicastAcknowledgements(Request req) {
		// Specify that the request is acknowledgement
		req.setReqType("Ack");
		for (int peerServerID : peerServerSocketMap.keySet()) {
			Socket s = peerServerSocketMap.get(peerServerID);
			// sendMessage(s, req);
			sendMessage(peerServerID, req);
		}
	}

	/**
	 * Sends the request object to the respective socket
	 * 
	 * @param socket
	 * @param req
	 */
	// public void sendMessage(Socket socket, Request req) {
	//
	// try {
	// OutputStream rawOut = socket.getOutputStream();
	// InputStream rawIn = socket.getInputStream();
	// ObjectOutputStream out = new ObjectOutputStream(rawOut);
	// ObjectInputStream in = new ObjectInputStream(rawIn);
	//
	// out.writeObject(req);
	// // String status = (String) in.readObject();
	// // System.out.println(status);
	//
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

	public void sendMessage(int peerServerID, Request req) {
		try {
			peerServerOuputStreamMap.get(peerServerID).writeObject(req);
			// String status = (String) in.readObject();
			// System.out.println(status);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// public void receiveMessages();

}
