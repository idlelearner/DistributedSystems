public class ServerDetails {
	private String ServerHostName;
	private int ID;
	private int clientport;
	private int peerServerPort;

	public ServerDetails() {
	}

	public ServerDetails(String serverHostName, int iD, int clientport,
			int peerServerPort) {
		super();
		ServerHostName = serverHostName;
		ID = iD;
		this.clientport = clientport;
		this.peerServerPort = peerServerPort;
	}

	public String getServerHostName() {
		return ServerHostName;
	}

	public void setServerHostName(String serverHostName) {
		ServerHostName = serverHostName;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public int getClientport() {
		return clientport;
	}

	public void setClientport(int clientport) {
		this.clientport = clientport;
	}

	public int getPeerServerPort() {
		return peerServerPort;
	}

	public void setPeerServerPort(int peerServerPort) {
		this.peerServerPort = peerServerPort;
	}

	public String toString() {
		return "ServerDetails : " + ServerHostName + " : " + ID + " : "
				+ clientport + " : " + peerServerPort;
	}

}
