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

	public ServerManager(int serverID, BankOperations bankOperations,
			List<ServerDetails> peerServerList) {
		this.bankOperations = bankOperations;
		// TODO : Write loaders to load the bank account.
		repManager = new ReplicationManager();
		log = ServerLogger.getInstance();
		lamportClockCounter = 0.0;
		reqQueue = new PriorityQueue<Request>();
		this.serverID = serverID;

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
		incrementClock();
		req.setLamportClock(getLamportClockCounter());
		req.setAckCount(1);
		req.setSourceServerID(serverID);
		reqQueue.add(req);
		repManager.multiCastMessage(req);
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
			// TODO Auto-generated method stub
			while (true) {
				
			}
		}
	}

}
