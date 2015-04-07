import java.io.Serializable;

/**
 * Request object send from the client consisting of parameters and transaction
 * type.
 * 
 * @author dhass
 *
 */
public class ClientRequest implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String transactionType;
	Parameter params;

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

	public String toString() {
		return "TransactionType : " + transactionType + " : Params : " + params;
	}
}
