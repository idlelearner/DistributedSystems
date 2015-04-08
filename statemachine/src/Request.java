import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Request object to be passed across server.
 * 
 * @author thirunavukarasu
 *
 */
public class Request implements Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// To mention whether the request is propagation or acknowledgement.
	// It can be new or Ack.
	String reqType;

	int sourceServerID;
	double sourceServerClock;

	int senderServerID;
	double senderServerClock;

	// List of server IDs which acknowledged this request.
	CopyOnWriteArrayList<Integer> ackList = new CopyOnWriteArrayList<>();

	boolean acknowledged = false;
	// Request from the client holding the operation.
	ClientRequest clientRequest;

	public Request() {
	}

	public Request(String reqType, int sourceServerID,
			double sourceServerClock, int senderServerID,
			double senderServerClock, CopyOnWriteArrayList<Integer> ackList,
			boolean acknowledged, ClientRequest clientRequest) {
		super();
		this.reqType = reqType;
		this.sourceServerID = sourceServerID;
		this.sourceServerClock = sourceServerClock;
		this.senderServerID = senderServerID;
		this.senderServerClock = senderServerClock;
		this.ackList = ackList;
		this.acknowledged = acknowledged;
		this.clientRequest = clientRequest;
	}

	public String getReqType() {
		return reqType;
	}

	public void setReqType(String reqType) {
		this.reqType = reqType;
	}

	public int getSourceServerID() {
		return sourceServerID;
	}

	public void setSourceServerID(int sourceServerID) {
		this.sourceServerID = sourceServerID;
	}

	public double getSourceServerClock() {
		return sourceServerClock;
	}

	public void setSourceServerClock(double sourceServerClock) {
		this.sourceServerClock = sourceServerClock;
	}

	public int getSenderServerID() {
		return senderServerID;
	}

	public void setSenderServerID(int senderServerID) {
		this.senderServerID = senderServerID;
	}

	public double getSenderServerClock() {
		return senderServerClock;
	}

	public void setSenderServerClock(double senderServerClock) {
		this.senderServerClock = senderServerClock;
	}

	public ClientRequest getClientRequest() {
		return clientRequest;
	}

	public void setClientRequest(ClientRequest clientRequest) {
		this.clientRequest = clientRequest;
	}

	public CopyOnWriteArrayList<Integer> getAckList() {
		return ackList;
	}

	public void setAckList(CopyOnWriteArrayList<Integer> ackList) {
		this.ackList = ackList;
	}

	public boolean isAcknowledged() {
		return acknowledged;
	}

	public void setAcknowledged(boolean acknowledged) {
		this.acknowledged = acknowledged;
	}

	/**
	 * Clone the request object.
	 */
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public String toString() {
		return "ReqType : " + reqType + " : sourceServerID : " + sourceServerID
				+ " sourceServerClock : " + sourceServerClock
				+ " : senderServerID : " + senderServerID
				+ " senderServerClock : " + senderServerClock + " : ackList"
				+ ackList + " : clientRequest : " + clientRequest
				+ " : acknowledged : " + acknowledged;
	}
}
