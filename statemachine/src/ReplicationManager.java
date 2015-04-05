import java.util.ArrayList;
import java.util.List;

public class ReplicationManager {
	List<ServerDetails> peerServerDetails;

	public ReplicationManager() {
		peerServerDetails = new ArrayList<ServerDetails>();
	}

	public ReplicationManager(List<ServerDetails> peerServers) {
		peerServerDetails = peerServers;

		// Establish Connection between peer servers.

		// Wait for connections.

	}

	// public void startConnectionWithPeerServers()

	// public void multiCastMessage();

	// public void receiveMessages();

}
