package clientserver;

import java.io.Serializable;

public class Account implements Serializable {
	int acctID;
	double balance;

	public Account(int acctID, double balance) {
		super();
		this.acctID = acctID;
		this.balance = balance;
	}

	public int getAcctID() {
		return acctID;
	}

	public void setAcctID(int acctID) {
		this.acctID = acctID;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

}
