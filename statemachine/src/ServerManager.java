import java.io.ObjectOutputStream;
import java.util.Comparator;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.PriorityBlockingQueue;

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
	private PriorityBlockingQueue<Request> reqQueue;
	private int serverID;
	ConcurrentHashMap<Double, Request> requestMap;
	Hashtable<Double, ObjectOutputStream> requestOuputStreamMap;
	Hashtable<Double, Long> reqReceiveTimeMap;
	Hashtable<Double, Long> reqServicedTimeMap;
	Hashtable<Double, CopyOnWriteArrayList<Integer>> tempAckMap;
	ServerOperationExecutor executor;

	public ServerManager(int serverID, List<ServerDetails> peerServerList) {
		log = ServerLogger.getInstance(serverID);
		reqReceiveTimeMap = new Hashtable<Double, Long>();
		reqServicedTimeMap = new Hashtable<Double, Long>();
		bankOperations = new BankOperations();
		// Queue to order the updates based on the lamport clock
		reqQueue = new PriorityBlockingQueue<Request>(10000,
				new Comparator<Request>() {
					@Override
					public int compare(Request r1, Request r2) {
						return Double.compare(r1.getSourceServerClock(),
								r2.getSourceServerClock());
					}
				});

		// Map for easy updation of the request objects.
		requestMap = new ConcurrentHashMap<>();
		requestOuputStreamMap = new Hashtable<>();
		tempAckMap = new Hashtable<>();

		this.serverID = serverID;
		// Clockvalue starts as 0.1 for server1, 0.2 - server2.
		System.out.println("Server ID : " + serverID);
		lamportClockCounter = (double) ((serverID) / 10.0);
		System.out.println("Init Lamport clock value : " + lamportClockCounter);
		// Loaders to create accounts with balance.
		initCreateAccounts();
		System.out.println("Server : user accounts created");
		System.out.println("Ready to accept requests");
		repManager = new ReplicationManager(peerServerList);

		// Start the executor thread.
		executor = new ServerOperationExecutor(repManager, bankOperations);
		executor.start();
	}

	public int getServerId() {
		return serverID;
	}

	/**
	 * Loader to initialize with 10 accts and deposit $1000
	 */
	public void initCreateAccounts() {
		int amount = 1000;
		for (int i = 1; i <= 10; i++) {
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
	public synchronized void incrementClock() {
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
		lamportClockCounter = (double) ((double) (int) clock)
				+ (serverID / 10.0);
	}

	/**
	 * Add the request to the current server
	 * 
	 * @param req
	 */
	public void addToRequestQueue(ClientRequest req, ObjectOutputStream out) {
		// On receiving the request from client increment the lamport clock
		// Increment lamport clock

		// TODO : Remove Sys out statements
		// System.out.println(serverID + " received client request : " + req
		// + "\n");
		incrementClock();

		Request request = new Request();
		request.setClientRequest(req);
		request.setSourceServerID(serverID);
		Double curLamportClock = getLamportClockCounter();
		request.setSourceServerClock(curLamportClock);

		Date curTime = new java.util.Date();
		// Log the request in the form required to the server log file
		log.write(serverID + "  CLNT-REQ  " + curTime + "  "
				+ request.getSourceServerClock() + "  " + req.transactionType
				+ "  " + req.params);
		
		// record the time when the request has been received
				reqReceiveTimeMap.put(request.getSourceServerClock(), new Long(
						new java.util.Date().getTime()));

		requestMap.put(curLamportClock, request);
		// Store the output stream to a map for sending back the response
		requestOuputStreamMap.put(curLamportClock, out);
		incrementClock();
		request.setSenderServerID(serverID);
		request.setSenderServerClock(getLamportClockCounter());

		// Set the acknowledgement for the current server.
		request.getAckList().add(serverID);
		reqQueue.add(request);

		// System.out.println(serverID + " : addToQueue : " + request);
		// multicast the request to all other servers.
		repManager.multiCastMessage(request);
	}

	/**
	 * When a request from different server is received. Add the request to the
	 * queue.
	 * 
	 * @param req
	 */
	public synchronized void receiveRequest(Request req) {
		// check if the received request has smaller lamport value.
		// If yes, multicast ack.
		// System.out.println(serverID + " received server request : " + req
		// + "\n");

		Date curTime = new java.util.Date();
		// Log the request in the form required to the server log file
		log.write(serverID + "  SRV-REQ (" + req.getReqType() + ") " + curTime + "  "
				+ req.getSourceServerClock() + "  "
				+ req.getClientRequest().transactionType + "  "
				+ req.getClientRequest().params);

		// Update the current server lamport clock if needed
		if (req.getSenderServerClock() > getLamportClockCounter()) {
			setLamportClockCounter(req.getSenderServerClock());
		}

		if (req.getReqType().equals("New")) {
			
			req.setSenderServerID(serverID);
			// If the current request is lesser than the server's clock - should
			// be taken from the head of the queue??
			// Acknowledge the reqs Eg: 1.1 < 1.2
			if (reqQueue.isEmpty()
					|| req.getSourceServerClock() < reqQueue.peek()
							.getSourceServerClock()) {
				req.getAckList().add(serverID);
				req.setReqType("Ack");
				repManager.multicastAcknowledgements(req);
			} else {
				setLamportClockCounter(req.getSourceServerClock());
			}
			requestMap.put(req.getSourceServerClock(), req);
			// System.out.println("Added " + req.getSourceServerClock()
			// + " reqMap");
			incrementClock();
			req.setSenderServerID(serverID);
			req.setSenderServerClock(getLamportClockCounter());

			// If there are received acknowledgements for this new request
			// Append them.
			if (tempAckMap.containsKey(req.getSourceServerClock())) {
				for (int ackServerID : tempAckMap.get(req
						.getSourceServerClock())) {
					if (!req.getAckList().contains(ackServerID)) {
						req.getAckList().add(ackServerID);
						// System.out.println("Adding acknowledged server "
						// + ackServerID + " to new "
						// + req.getSourceServerClock());
					}
				}
				tempAckMap.remove(req.getSourceServerClock());
			}
			reqQueue.add(req);

		} else {

			// If the received request is ack, update the request obj.
			// Remote process has acknowledged.
			incrementClock();

			// If the received request is ack for request from other servers and
			// the new request has not
			// yet arrived,then add it to a temporary map
			if (req.getSourceServerID() != serverID
					&& !requestMap.containsKey(req.getSourceServerClock())) {
				if (!tempAckMap.containsKey(req.getSourceServerClock())) {
					tempAckMap.put(req.getSourceServerClock(),
							new CopyOnWriteArrayList<Integer>());
				}
				if (!tempAckMap.get(req.getSourceServerClock()).contains(
						req.getSourceServerClock())) {
					// System.out.println("Adding " + req.getSourceServerClock()
					// + " to tmpMap with Server"
					// + req.getSenderServerID());
					tempAckMap.get(req.getSourceServerClock()).add(
							req.getSenderServerID());
				}
			} else {
				if (requestMap.containsKey(req.getSourceServerClock()))
					requestMap.get(req.getSourceServerClock()).getAckList()
							.add(req.getSenderServerID());
				// If the request has acknowledgement from all the servers set
				// acknowledged flag.
				if (req.getAckList().size() >= 3 || req.isAcknowledged())
					requestMap.get(req.getSourceServerClock()).setAcknowledged(
							true);
			}
		}
		// System.out.println("Head of the queue : " + reqQueue.peek());
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
		case "HALT":
			status.append(haltServer());
			break;
		default:
			status.append("Operation not supported!");
			break;
		}

		return status.toString();
	}

	public void executeOperation(Request req) {
		String response = performOperation(req.getClientRequest());
		// record the time when the request has been serviced into the map
		reqServicedTimeMap.put(req.getSourceServerClock(), new Long(
				new java.util.Date().getTime()));

		System.out.println(serverID + " executed req : " + req);
		// Remove the request for request map.
		requestMap.remove(req.getSourceServerClock());
		// If the request was from this server, output has to be send back to
		// the client.
		if (req.getSourceServerID() == serverID) {
			try {
				requestOuputStreamMap.get(req.getSourceServerClock())
						.writeObject(response);
				// after sending the response, remove the outputstream.
				requestOuputStreamMap.remove(req.getSourceServerClock());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * performs the halt operations and sends back the response string
	 * 
	 * @return
	 */
	public String haltServer() {
		// print final stats
		printFinalStats();

		// flush server logs and close the logger, simply write "HALT"
		log.write("HALT");

		return "HALT Successful";
	}

	/**
	 * Method to print the final stats of the server when HALT is called on the
	 * server.
	 */
	public void printFinalStats() {
		// TODO : When the client calls halt
		// print the avg response time for requests
		Iterator it = reqReceiveTimeMap.entrySet().iterator();
		int count = 0;
		long totalResponseTime = 0;
		while (it.hasNext()) {
			Map.Entry<Double, Long> pair = (Map.Entry) it.next();
			// see if there is an entry in the response map
			if (reqServicedTimeMap.containsKey(pair.getKey())) {
				totalResponseTime += ((Long) reqServicedTimeMap.get(pair
						.getKey())).longValue()
						- ((Long) pair.getValue()).longValue();
				count++;
			}
		}

		// TODO: remove this
		System.out.println("Total number of requests processed : " + count);

		// print out the average response time
		System.out.println("Average response time = " + totalResponseTime
				/ count + " milliseconds");

		// print the current balance in each account
		for (int i = 1; i <= 10; i++) {
			System.out.println("Current balance in Account (ID = " + i + ") = "
					+ bankOperations.getBalance(i));
		}

	}

	/**
	 * Thread to check the queue and execute the operation.
	 * 
	 * @author thirunavukarasu
	 *
	 */
	class ServerOperationExecutor extends Thread {
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
				if (!reqQueue.isEmpty()) {
					// If the request got all acknowledgement then execute the
					// operation

					if (reqQueue.peek().getAckList().size() >= 3
							|| reqQueue.peek().isAcknowledged()) {
						Request polledRequest = reqQueue.poll();

						Date curTime = new java.util.Date();
						// Log the request in the form required to the server
						// log file
						log.write(serverID + "  PROCESS  " + curTime + "  "
								+ polledRequest.getSourceServerClock());
						executeOperation(polledRequest);
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
							reqQueue.peek().setAcknowledged(true);
							Request r;
							try {
								r = (Request) reqQueue.peek().clone();
								repManager.multicastAcknowledgements(r);
							} catch (CloneNotSupportedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				} else
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}

		}
	}

}
