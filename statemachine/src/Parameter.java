import java.io.Serializable;

/**
 * Parameter object embedded in request object to send parameters to the server
 * 
 * @author dhass
 *
 */
public class Parameter implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int acctID;
	String firstname;
	String lastname;
	String address;
	int amt;
	int srcAcctID;
	int destAcctID;

	public Parameter() {

	}

	public Parameter(String firstname, String lastname, String address) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.address = address;
	}

	public Parameter(int acctID, int amt) {
		this.acctID = acctID;
		this.amt = amt;
	}

	public Parameter(int acctID) {
		this.acctID = acctID;
	}

	public Parameter(int srcAcctID, int destAcctID, int amt) {
		this.srcAcctID = srcAcctID;
		this.destAcctID = destAcctID;
		this.amt = amt;
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

	public int getAmt() {
		return amt;
	}

	public void setAmt(int amt) {
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

	public String toString() {
		return "[acctID: " + acctID + "," + "firstname: " + firstname + ","
				+ "lastname: " + lastname + "," + "address: " + address + ","
				+ "amt: " + amt + "," + "srcAcctID: " + srcAcctID + ","
				+ "destAcctID: " + destAcctID;
	}
}
