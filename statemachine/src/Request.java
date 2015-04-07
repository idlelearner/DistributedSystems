import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Request object to be passed across server.
 * 
 * @author thirunavukarasu
 *
 */
public class Request implements Serializable {

	// To mention whether the request is propagation or acknowledgement.
	String reqType;

	int sourceServerID;
	double sourceServerClock;

	int senderServerID;
	double senderServerClock;
	List<Integer> ackList = new ArrayList<>();
	// Request from the client holding the operation.
	ClientRequest clientRequest;

	public Request() {
	}

	public Request(String reqType, int sourceServerID,
			double sourceServerClock, int senderServerID,
			double senderServerClock, List<Integer> ackList,
			ClientRequest clientRequest) {
		super();
		this.reqType = reqType;
		this.sourceServerID = sourceServerID;
		this.sourceServerClock = sourceServerClock;
		this.senderServerID = senderServerID;
		this.senderServerClock = senderServerClock;
		this.clientRequest = clientRequest;
		this.ackList = ackList;
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

	public List<Integer> getAckList() {
		return ackList;
	}

	public void setAckList(List<Integer> ackList) {
		this.ackList = ackList;
	}

}
