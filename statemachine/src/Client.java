import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class Client {
	protected String host, file;
	protected int port;
	protected DataInputStream in;
	protected DataOutputStream out;
	protected ClientLogger log = ClientLogger.getInstance();

	public static void main(String[] args) throws UnknownHostException,
			IOException, ClassNotFoundException, InterruptedException {

		if (args.length < 1) {
			throw new RuntimeException(
					"Config file has to be mentioned as argument");
		}

		String configFile = args[0];
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

		// Create threads to transfer amount
		List<TransferClient> tcList = new ArrayList<TransferClient>();
		for (int i = 0; i < serverConfigList.size(); i++) {
			System.out.println("Creating clients for "
					+ serverConfigList.get(i).getClientport());
			TransferClient tc = new TransferClient(serverConfigList.get(i)
					.getServerHostName(), serverConfigList.get(i)
					.getClientport());
			tc.start();
			tcList.add(tc);
		}

		// Wait till all other threads finish
		for (int i = 0; i < serverConfigList.size(); i++)
			tcList.get(i).join();

		// Send a request to the main server (0) to HALT
		sendHALTRequest(serverConfigList);

	}

	/**
	 * Called by the main thread to send a HALT request to server with id 1
	 * 
	 * @param serverConfigList
	 */
	public static void sendHALTRequest(ArrayList<ServerDetails> serverConfigList) {
		Socket socket;
		try {
			socket = new Socket(serverConfigList.get(1).getServerHostName(),
					serverConfigList.get(1).getClientport());
			OutputStream rawOut = socket.getOutputStream();
			InputStream rawIn = socket.getInputStream();
			ObjectOutputStream out = new ObjectOutputStream(rawOut);
			ObjectInputStream in = new ObjectInputStream(rawIn);

			ClientRequest halt = new ClientRequest();
			halt.transactionType = "HALT";
			halt.params = new Parameter();

			out.writeObject(halt);

			in.close();

			socket.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
