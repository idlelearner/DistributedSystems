import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * class to maintain the lamport clocks and apply updates.
 * 
 * @author thirunavukarasu
 *
 */
public class ServerManager {

	private BankOperations bankOperations;
	private ServerLogger log;
	private ReplicationManager repManager;
	private Double lamportClockCounter;
	private PriorityQueue<Request> reqQueue;
	private int serverID;
	Map<Double, Request> requestMap;
	Map<Double, ObjectOutputStream> requestOuputStreamMap;

	public ServerManager(int serverID, List<ServerDetails> peerServerList) {
		log = ServerLogger.getInstance();
		bankOperations = new BankOperations();
		// Queue to order the updates based on the lamport clock
		reqQueue = new PriorityQueue<Request>(10000, new Comparator<Request>() {
			@Override
			public int compare(Request r1, Request r2) {
				return Double.compare(r1.getSourceServerClock(),
						r2.getSourceServerClock());
			}
		});

		// Map for easy updation of the request objects.
		requestMap = new HashMap<>();
		requestOuputStreamMap = new HashMap<>();
		this.serverID = serverID;
		// Clockvalue starts as 0.1 for server1, 0.2 - server2.
		System.out.println("Server ID : " + serverID);
		lamportClockCounter = (double) ((serverID)/10.0);
		System.out.println("Init Lamport clock value : " + lamportClockCounter);
		// Loaders to create accounts with balance.
		initCreateAccounts();
		System.out.println("Server : user accounts created");
		System.out.println("Ready to accept requests");
		repManager = new ReplicationManager(peerServerList);
	}

	/**
	 * Loader to initialize with 10 accts and deposit $1000
	 */
	public void initCreateAccounts() {
		int amount = 1000;
		for (int i = 1; i < 10; i++) {
			// Create accounts and deposit $1000
			int acctID = bankOperations.createAccount("first-" + i, "last" + i,
					"address" + i);
			bankOperations.deposit(acctID, amount);
		}
	}

	/**
	 * Start connections with all peer servers.
	 */
	public void establishConnectionWithPeers() {
		repManager.startConnectionWithPeerServers();
	}

	/**
	 * Increment the lamport clock value by 1.
	 */
	public void incrementClock() {
		lamportClockCounter++;
	}

	/**
	 * Gets the lamport clock value.
	 * 
	 * @return
	 */
	public double getLamportClockCounter() {
		return lamportClockCounter;
	}

	/**
	 * Set the server's lamport clock with the given clock and update the clock
	 * with server ID as decimal Eg : for server 2 => n.2
	 * 
	 * @param clock
	 */
	public void setLamportClockCounter(double clock) {
		lamportClockCounter = (double) ((int) clock) + serverID / 10;
	}

	/**
	 * Add the request to the current server
	 * 
	 * @param req
	 */
	public synchronized void addToRequestQueue(ClientRequest req,
			ObjectOutputStream out) {
		// On receiving the request from client increment the lamport clock
		// Increment lamport clock
		System.out.println(serverID + " received client request : " + req
				+ "\n");
		incrementClock();
		Request request = new Request();
		request.setClientRequest(req);
		request.setSourceServerID(serverID);
		request.setSourceServerClock(getLamportClockCounter());
		// Set the acknowledgement for the current server.
		request.getAckList().add(serverID);
		reqQueue.add(request);
		requestMap.put(getLamportClockCounter(), request);
		// Store the output stream to a map for sending back the response
		requestOuputStreamMap.put(getLamportClockCounter(), out);
		// multicast the request to all other servers.
		repManager.multiCastMessage(request);
	}

	/**
	 * When a request from different server is received. Add the request to the
	 * queue.
	 * 
	 * @param req
	 */
	public void receiveRequest(Request req) {
		// check if the received request has smaller lamport value.
		// If yes, multicast ack.
		System.out.println(serverID + " received server request : " + req
				+ "\n");

		if (req.getReqType().equals("New")) {
			if (req.getSourceServerClock() < getLamportClockCounter()) {
				req.getAckList().add(serverID);
				req.setReqType("Ack");
				repManager.multicastAcknowledgements(req);
			} else {
				setLamportClockCounter(req.getSourceServerClock());
			}
			requestMap.put(getLamportClockCounter(), req);
			incrementClock();
			req.setSenderServerID(serverID);
			req.setSenderServerClock(getLamportClockCounter());
			reqQueue.add(req);

		} else {
			// Update the current server lamport clock.
			if (req.getSourceServerClock() > getLamportClockCounter()) {
				setLamportClockCounter(req.getSenderServerClock());
			}
			// If the received request is ack, update the request obj.
			// Remote process has acknowledged.
			incrementClock();
			requestMap.get(req.getSourceServerClock()).getAckList()
					.add(req.getSenderServerID());
		}

	}

	/**
	 * Calls the respective method to perform operations on the data store -
	 * Bank operations
	 * 
	 * @param request
	 * @return
	 */
	public String performOperation(ClientRequest request) {
		StringBuilder status = new StringBuilder();
		Parameter param = request.getParams();
		switch (request.getTransactionType()) {
		case "createAcct":
			status.append(bankOperations.createAccount(param.getFirstname(),
					param.getLastname(), param.getAddress()));
			break;
		case "deposit":
			status.append(bankOperations.deposit(param.getAcctID(),
					param.getAmt()));
			break;
		case "withdraw":
			status.append(bankOperations.withdraw(param.getAcctID(),
					param.getAmt()));
			break;

		case "getBalance":
			status.append(bankOperations.getBalance(param.getAcctID()));
			break;
		case "transfer":
			status.append(bankOperations.transfer(param.getSrcAcctID(),
					param.getDestAcctID(), param.getAmt()));
			break;
		case "HALT" :
			status.append(haltServer());
			break;
		default:
			status.append("Operation not supported!");
			break;
		}
		log.write("Server Response :" + status.toString());
		return status.toString();
	}

	public void executeOperation(Request req) {
		String response = performOperation(req.getClientRequest());
		// If the request was from this server, output has to be send back to
		// the client.
		if (req.getSourceServerID() == serverID) {
			try {
				requestOuputStreamMap.get(req.getSourceServerClock())
						.writeObject(response);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * performs the halt operations and sends back the response string
	 * @return
	 */
	public String haltServer() {
		//print final stats
		printFinalStats();
		
		//close the logger
		closeServerLogger();
		
		return "HALT Successful";
	}
	
	public void closeServerLogger() {
		//close server loggers since now we do not need them anymore
		
	}

	/**
	 * Method to print the final stats of the server when HALT is called on the
	 * server.
	 */
	public void printFinalStats() {
		// TODO : When the client calls halt
		// print the avg response and balance in all the accts.
	}

	/**
	 * Thread to check the queue and execute the operation.
	 * 
	 * @author thirunavukarasu
	 *
	 */
	class ServerOperationExecutor implements Runnable {
		ReplicationManager repManager;
		BankOperations bankOperations;

		public ServerOperationExecutor(ReplicationManager repManager,
				BankOperations bankOperations) {
			this.repManager = repManager;
			this.bankOperations = bankOperations;
		}

		@Override
		public void run() {
			while (true) {
				// if the request got
				if (reqQueue.peek().getAckList().size() >= 3) {
					executeOperation(reqQueue.poll());
				} else {
					// If the request is at the head of the queue and the
					// current have not acknowledged it, then acknowledge.
					if (!reqQueue.peek().getAckList().contains(serverID)) {
						reqQueue.peek().getAckList().add(serverID);
						// Increment the lamport clock
						// before sending the acknowledgements
						incrementClock();
						reqQueue.peek().setSenderServerClock(
								getLamportClockCounter());
						repManager.multicastAcknowledgements(reqQueue.peek());
					}
				}
			}
		}
	}

}
