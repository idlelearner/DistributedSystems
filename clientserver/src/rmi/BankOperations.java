package rmi;

import java.util.Hashtable;

//Bank operations defined on an account
public class BankOperations {
	Hashtable<Integer, Account> accountMap;
	volatile int maxAcctID;

	public BankOperations() {
		maxAcctID = 1;
		accountMap = new Hashtable<Integer, Account>();
	}

	public synchronized int createAccount(String firstname, String lastname,
			String address) {
		Account acct = new Account(getNewAccountID(), firstname, lastname,
				address, 0);
		accountMap.put(acct.getAcctID(), acct);
		return acct.getAcctID();
	}

	private synchronized int getNewAccountID() {
		return maxAcctID++;
	}

	public synchronized String deposit(int acctID, int amt) {
		int prevBalance = accountMap.get(acctID).getBalance();
		int curBalance = prevBalance + amt;
		accountMap.get(acctID).setBalance(curBalance);
		return "Deposit sucessful";
	}

	public synchronized String withdraw(int acctID, int amt) {
		int prevBalance = accountMap.get(acctID).getBalance();
		if (prevBalance < amt)
			return "No sufficient balance";
		int curBalance = prevBalance - amt;
		accountMap.get(acctID).setBalance(curBalance);
		return "Withdrawal sucessful";
	}

	public synchronized int getBalance(int acctID) {
		int balance = accountMap.get(acctID).getBalance();
		return balance;
	}

	public synchronized String transfer(int srcAcctID, int destAcctID,
			int amt) {
		String withdrawStatus = withdraw(srcAcctID, amt);
		if (!withdrawStatus.equals("Withdrawal sucessful"))
			return "No sufficient balance in source account";
		deposit(destAcctID, amt);
		return "Transfer Sucessful";
	}

}