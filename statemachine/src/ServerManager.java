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

	public ServerManager(int serverID, BankOperations bankOperations,
			List<ServerDetails> peerServerList) {
		this.bankOperations = bankOperations;
		//TODO : Write loaders to load the bank account.
		repManager = new ReplicationManager();
		log = ServerLogger.getInstance();
		lamportClockCounter = 0.0;
		reqQueue = new PriorityQueue<Request>();
	}

	/**
	 * Increment the lamport clock
	 */
	public void incrementClock() {
		lamportClockCounter++;
	}

	public double getLamportClockCounter() {
		return lamportClockCounter;
	}

	/**
	 * Add the request to the queue
	 * 
	 * @param req
	 */
	public void addToRequestQueue(Request req) {
		incrementClock();
		req.setLamportClock(getLamportClockCounter());
		reqQueue.add(req);
	}

	/**
	 * Calls the respective method to perform some operation
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
}
