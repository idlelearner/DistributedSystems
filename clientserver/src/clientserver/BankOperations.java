package clientserver;

import java.util.Hashtable;

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
				address, 0.0);
		accountMap.put(acct.getAcctID(), acct);
		System.out.println(accountMap);
		return acct.getAcctID();
	}

	public synchronized int getNewAccountID() {
		return maxAcctID++;
	}

	public synchronized String deposit(int acctID, double amt) {
		double prevBalance = accountMap.get(acctID).getBalance();
		double curBalance = prevBalance + amt;
		accountMap.get(acctID).setBalance(curBalance);
		return "Deposit sucessful";
	}

	public synchronized String withdraw(int acctID, double amt) {
		double prevBalance = accountMap.get(acctID).getBalance();
		if (prevBalance < amt)
			return "No sufficient balance";
		double curBalance = prevBalance - amt;
		accountMap.get(acctID).setBalance(curBalance);
		return "Withdrawal sucessful";
	}

	public synchronized double getBalance(int acctID) {
		return accountMap.get(acctID).getBalance();
	}

	public synchronized String transfer(int srcAcctID, int destAcctID,
			double amt) {
		String withdrawStatus = withdraw(srcAcctID, amt);
		if (!withdrawStatus.equals("Withdrawal sucessful"))
			return "No sufficient balance in source account";
		deposit(destAcctID, amt);
		return "Transfer Sucessful";
	}

}
