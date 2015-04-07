import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * class to maintain the lamport clocks and apply updates.
 * 
 * @author thirunavukarasu
 *
 */
public class ServerManager {

	protected BankOperations bankOperations;
	protected ServerLogger log;
	ReplicationManager repManager;
	protected Double lamportClockCounter;
	PriorityQueue<Request> reqQueue;
	protected int serverID;

	public ServerManager(int serverID, List<ServerDetails> peerServerList) {
		repManager = new ReplicationManager();
		log = ServerLogger.getInstance();
		lamportClockCounter = 0.0;
		bankOperations = new BankOperations();
		reqQueue = new PriorityQueue<Request>(10000, new Comparator<Request>() {
			@Override
			public int compare(Request r1, Request r2) {
				return Double.compare(r1.getLamportClock(),
						r2.getLamportClock());
			}
		});

		// TODO : Need to add a map for easy updation of the request objects.
		// <CLock, Request>

		this.serverID = serverID;
		// Loaders to create accounts with balance.
		initCreateAccounts();
		log.write("Server : user accounts created");
		log.write("Ready to accept requests");
	}

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
	 * Increment the lamport clock value
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
	 * Add the request to the current server
	 * 
	 * @param req
	 */
	public void addToRequestQueue(Request req) {
		// On receiving the request from client increment the lamport clock
		incrementClock();
		req.setLamportClock(getLamportClockCounter());
		req.setAckCount(1);
		req.setSourceServerID(serverID);
		reqQueue.add(req);
		repManager.multiCastMessage(req);
	}

	/**
	 * When a request from different server is received. Add the request to the
	 * queue.
	 * 
	 * @param req
	 */
	public void receiveRequest(Request req) {
		reqQueue.add(req);
	}

	/**
	 * Calls the respective method to perform operations on the data store -
	 * Bank operations
	 * 
	 * @param request
	 * @return
	 */
	public String performOperation(Request request) {
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
		default:
			status.append("Operation not supported!");
			break;
		}
		log.write("Server Response :" + status.toString());
		return status.toString();
	}

	/**
	 * Thread to check the queue and execute the operation.
	 * 
	 * @author thirunavukarasu
	 *
	 */
	class ServerOperationExecuter implements Runnable {
		ReplicationManager repManager;
		BankOperations bankOperations;

		public ServerOperationExecuter(ReplicationManager repManager,
				BankOperations bankOperations) {
			this.repManager = repManager;
			this.bankOperations = bankOperations;
		}

		@Override
		public void run() {
			while (true) {
				// if the request got
				if (reqQueue.peek().sourceServerID == serverID) {
					if (reqQueue.peek().getAckCount() >= 3) {
						performOperation(reqQueue.poll());
					}
				} else {
					// check for the other case where the request has arrived
					// from different server.
					// When to execute??

				}
			}
		}
	}

}
