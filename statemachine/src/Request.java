import java.io.Serializable;

/**
 * Request object send from the client
 * 
 * @author dhass
 *
 */
public class Request implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String transactionType;
	Parameter params;
	Double lamportClock;

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public Parameter getParams() {
		return params;
	}

	public void setParams(Parameter params) {
		this.params = params;
	}

	public Double getLamportClock() {
		return lamportClock;
	}

	public void setLamportClock(Double lamportClock) {
		this.lamportClock = lamportClock;
	}

}
