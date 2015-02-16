package clientserver;

import java.io.Serializable;

public class Request implements Serializable {
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

}
