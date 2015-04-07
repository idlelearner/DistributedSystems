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
import java.net.InetAddress;
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
			TransferClient tc = new TransferClient(serverConfigList.get(0)
					.getServerHostName(), serverConfigList.get(0)
					.getClientport());
			tc.start();
			tcList.add(tc);
		}

		// Wait till all other threads finish
		for (int i = 0; i < serverConfigList.size(); i++)
			tcList.get(i).join();

	}

	/**
	 * Function create accts
	 * 
	 * @param noOfAccts
	 * @param out
	 * @param in
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public List<Integer> createAccts(int noOfAccts, ObjectOutputStream out,
			ObjectInputStream in) throws IOException, ClassNotFoundException {
		int i = 1;
		List<Integer> accts = new ArrayList<Integer>();
		while (i <= noOfAccts) {
			ClientRequest createAcct = new ClientRequest();
			createAcct.transactionType = "createAcct";
			createAcct.params = new Parameter("\nF" + i, "L" + i, "A" + i);
			out.writeObject(createAcct);
			String acct = (String) in.readObject();
			int acctID = Integer.parseInt(acct);
			accts.add(acctID);
			i++;
		}
		return accts;
	}

	/**
	 * Deposit amount
	 * 
	 * @param accts
	 * @param amt
	 * @param out
	 * @param in
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void deposit(List<Integer> accts, int amt, ObjectOutputStream out,
			ObjectInputStream in) throws IOException, ClassNotFoundException {
		for (int acct : accts) {
			ClientRequest deposit = new ClientRequest();
			deposit.transactionType = "deposit";
			deposit.params = new Parameter(acct, amt);
			out.writeObject(deposit);
			String status = (String) in.readObject();
		}
	}

	/**
	 * Get balance for acct
	 * 
	 * @param accts
	 * @param out
	 * @param in
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public int getTotalBalance(List<Integer> accts, ObjectOutputStream out,
			ObjectInputStream in) throws IOException, ClassNotFoundException {
		int total = 0;
		for (int acct : accts) {
			ClientRequest getBalance = new ClientRequest();
			getBalance.transactionType = "getBalance";
			getBalance.params = new Parameter(acct);
			out.writeObject(getBalance);
			String s = (String) in.readObject();
			total = total + Integer.parseInt(s);
		}
		return total;
	}
}
