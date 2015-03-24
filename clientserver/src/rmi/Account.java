package rmi;

import java.io.Serializable;

//Class to store information for an account
public class Account implements Serializable {

	private int acctID;
	private String firstname;
	private String lastname;
	private String address;
	private int balance;

	public Account(int acctID, String firstname, String lastname,
			String address, int balance) {
		super();
		this.acctID = acctID;
		this.firstname = firstname;
		this.lastname = lastname;
		this.address = address;
		this.balance = balance;
	}

	public int getAcctID() {
		return acctID;
	}

	public void setAcctID(int acctID) {
		this.acctID = acctID;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getBalance() {
		return balance;
	}

	public void setBalance(int balance) {
		this.balance = balance;
	}

	public String toString() {
		return "[AccountID : " + acctID + ",firstname : " + firstname
				+ ",lastname : " + lastname + ",address : " + address
				+ ",balance : " + balance + "]";
	}
}