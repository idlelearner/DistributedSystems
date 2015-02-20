package clientserver;

import java.util.Hashtable;

/**
 * Class to maintain the account information and perform operations
 * 
 * @author dhass
 *
 */
public class BankOperations {
	Hashtable<Integer, Account> accountMap;
	volatile int maxAcctID;
	ServerLogger log;

	public BankOperations() {
		maxAcctID = 1;
		accountMap = new Hashtable<Integer, Account>();
		log = ServerLogger.getInstance();
	}

	public synchronized int createAccount(String firstname, String lastname,
			String address) {
		Account acct = new Account(getNewAccountID(), firstname, lastname,
				address, 0);
		accountMap.put(acct.getAcctID(), acct);
		// System.out.println(accountMap);
		return acct.getAcctID();
	}

	public synchronized int getNewAccountID() {
		return maxAcctID++;
	}

	public synchronized String deposit(int acctID, int amt) {
		int prevBalance = accountMap.get(acctID).getBalance();
		int curBalance = prevBalance + amt;
		accountMap.get(acctID).setBalance(curBalance);
		return "Deposit sucessful for acctID: " + acctID + " with amt: " + amt;
	}

	public synchronized String withdraw(int acctID, int amt) {
		int prevBalance = accountMap.get(acctID).getBalance();
		if (prevBalance < amt)
			return "No sufficient balance in acctID: " + acctID + " for amt: "
					+ amt;
		int curBalance = prevBalance - amt;
		accountMap.get(acctID).setBalance(curBalance);
		return "Withdrawal sucessful for acctID: " + acctID + " for amt: "
				+ amt;
	}

	public synchronized int getBalance(int acctID) {
		int balance = accountMap.get(acctID).getBalance();
		log.write("Balance of Acct acctID :" + acctID + " : " + balance);
		return balance;
	}

	public synchronized String transfer(int srcAcctID, int destAcctID, int amt) {
		String withdrawStatus = withdraw(srcAcctID, amt);
		if (!withdrawStatus.contains("Withdrawal sucessful"))
			return "No sufficient balance in source account acctID: "
					+ srcAcctID + " for amt: " + amt;
		deposit(destAcctID, amt);
		return "Transfer Sucessful from source account acctID: " + srcAcctID
				+ " to destination acct acctID :" + destAcctID + " for amt: "
				+ amt;
	}

}
