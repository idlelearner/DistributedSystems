import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server extends Thread {

	protected Socket s;
	protected BankOperations bankOperations;
	protected ServerManager serverManager;
	protected ServerLogger log;

	public Server(Socket s, ServerManager serverManager) {
		System.out.println("New client.");
		this.s = s;
		this.serverManager = serverManager;
		log = ServerLogger.getInstance();
	}

	public static void main(String[] args) throws FileNotFoundException,
			IOException {

		// Get the config file
		String configFile = args[0];
		// Server ID specify the current server ID.
		int serverID = Integer.parseInt(args[1]);

		ArrayList<ServerDetails> serverConfigList = new ArrayList<ServerDetails>();
		File file = new File(configFile);
		BufferedReader in = new BufferedReader(new FileReader(file));
		String line;
		while ((line = in.readLine()) != null) {
			if (line.length() > 0) {
				// Process line of input Here
				String params[] = line.split(" ");
				String hostname = params[0];
				int id = Integer.parseInt(params[1]);
				int clientPortID = Integer.parseInt(params[2]);
				int peerServerPortID = Integer.parseInt(params[3]);
				ServerDetails serverDetails = new ServerDetails(hostname, id,
						clientPortID, peerServerPortID);
				System.out.println(serverDetails);
				serverConfigList.add(serverDetails);
			}
		}

		// Get the peer server details.
		ArrayList<ServerDetails> peerServerList = new ArrayList<ServerDetails>();
		// Current server
		ServerDetails curServer = null;

		for (ServerDetails server : serverConfigList) {
			if (server.getID() != serverID) {
				peerServerList.add(server);
			} else
				curServer = server;
		}

		ServerManager serverManager = new ServerManager(curServer.getID(),
				peerServerList);

		// Connection handler for the peer servers.
		ServerSocket serverForPeerConnections = new ServerSocket(
				curServer.getPeerServerPort());
		PeerServerConnectionHandler peerConnections = new PeerServerConnectionHandler(
				serverForPeerConnections, serverManager);
		peerConnections.start();

		// Server socket to accept connections from clients
		ServerSocket serverForClientConnections = new ServerSocket(
				curServer.getClientport());

		while (true) {
			System.out.println("Waiting for a client request");
			Socket client = serverForClientConnections.accept();
			System.out.println("Received request from "
					+ client.getInetAddress());
			Server s = new Server(client, serverManager);
			s.start();
		}

	}

	// Start this server.
	public void run() {
		try {
			InputStream istream = s.getInputStream();
			OutputStream ostream = s.getOutputStream();
			ObjectInputStream in = new ObjectInputStream(istream);
			ObjectOutputStream out = new ObjectOutputStream(ostream);
			try {
				while (true) {
					Request r = (Request) in.readObject();
					log.write("Transaction type : " + r.getTransactionType());
					log.write("Parameter received : " + r.params);
					if (r.getTransactionType().contains("exit"))
						break;

					serverManager.addToRequestQueue(r);
					// TODO :How to send the response object back to client.
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
			log.write("Client exit.");
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

}
