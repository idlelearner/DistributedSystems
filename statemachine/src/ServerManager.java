import java.util.List;

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

	public ServerManager(BankOperations bankOperations,
			List<ServerDetails> peerServerList) {
		this.bankOperations = bankOperations;
		repManager = new ReplicationManager();
		log = ServerLogger.getInstance();
		lamportClockCounter = 0.0;
	}

	
	public void incrementClock() {

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
