package clientserver;

import java.io.Serializable;

public class Parameter implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int acctID;
	String firstname;
	String lastname;
	String address;
	double amt;
	int srcAcctID;
	int destAcctID;

	public Parameter() {

	}

	public Parameter(String firstname, String lastname, String address) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.address = address;
	}

	public Parameter(int acctID, double amt){
		this.acctID = acctID;
		this.amt = amt;
	}
	
	public Parameter(int acctID){
		this.acctID = acctID;
	}
	
	public Parameter(int srcAcctID, int destAcctID, double amt){
		this.srcAcctID = srcAcctID;
		this.destAcctID =  destAcctID;
		
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

	public double getAmt() {
		return amt;
	}

	public void setAmt(double amt) {
		this.amt = amt;
	}

	public int getSrcAcctID() {
		return srcAcctID;
	}

	public void setSrcAcctID(int srcAcctID) {
		this.srcAcctID = srcAcctID;
	}

	public int getDestAcctID() {
		return destAcctID;
	}

	public void setDestAcctID(int destAcctID) {
		this.destAcctID = destAcctID;
	}

}
