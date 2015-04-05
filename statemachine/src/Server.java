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

	public Server(Socket s, ServerManager serverManager,
			BankOperations bankOperations) {
		System.out.println("New client.");
		this.s = s;
		this.serverManager = serverManager;
		this.bankOperations = bankOperations;
		log = ServerLogger.getInstance();
	}

	public static void main(String[] args) throws FileNotFoundException,
			IOException {
		// TODO : Pass the config to the program
		// Read and form the object and start.

		String configFile = args[0];
		int serverID = Integer.parseInt(args[1]);

		// TODO : create the List of Server start files.

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

		// TODO : Get the peer server details.
		ArrayList<ServerDetails> peerServerList = new ArrayList<ServerDetails>();
		ServerDetails curServer = null;
		for (ServerDetails server : serverConfigList) {
			if (server.getID() != serverID) {
				peerServerList.add(server);
			} else
				curServer = server;
		}

		BankOperations bankOperations = new BankOperations();
		ServerManager serverManager = new ServerManager(curServer.getID(),
				bankOperations, peerServerList);
		ServerSocket serverForClientConnections = new ServerSocket(
				curServer.getClientport());
		ServerSocket serverForPeerConnections = new ServerSocket(
				curServer.getPeerServerPort());

		PeerServerConnectionHandler peerConnections = new PeerServerConnectionHandler(
				serverForPeerConnections, serverManager);

		while (true) {
			System.out.println("Waiting for a client request");
			Socket client = serverForClientConnections.accept();
			System.out.println("Received request from "
					+ client.getInetAddress());
			Server s = new Server(client, serverManager, bankOperations);
			s.start();
		}

	}

	// Start this server.
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
					Request r = (Request) in.readObject();
					log.write("Transaction type : " + r.getTransactionType());
					log.write("Parameter received : " + r.params);
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
