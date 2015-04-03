import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ServerStart {
	public static void main(String[] args) throws FileNotFoundException,
			IOException {
		// TODO : Pass the config to the program
		// Read and form the object and start.

		String configFile = args[0];
		// TODO : create the List of Server start files.

		ArrayList<ServerDetails> serverConfigList = new ArrayList<ServerDetails>();
		File file = new File(configFile);
		BufferedReader in = new BufferedReader(new FileReader(file));
		String line;
		while ((line = in.readLine()) != null) {
			if (line.length() > 0) {
				// Process line of input Here
				String params[] = line.split(" ");
				String hostname = params[0];
				int id = Integer.parseInt(params[1]);
				int clientPortID = Integer.parseInt(params[2]);
				int peerServerPortID = Integer.parseInt(params[3]);
				ServerDetails serverDetails = new ServerDetails(hostname, id,
						clientPortID, peerServerPortID);
				System.out.println(serverDetails);
				serverConfigList.add(serverDetails);
			}
		}

		//Add New server
		
		
//		for(int i)
		
	}
}
