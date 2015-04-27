import java.io.IOException;
import java.rmi.Naming;
import java.rmi.registry.Registry;

/**
 * Client class for parsing generating dictionary and communicating with chord ring
 * rmi calls made from the client
 * @author varun
 *
 */
public class Client {
	static protected Registry rmiReg;
	
	public static void main (String[] argv) throws IOException {
			
		//generate a node
		
		//parse the word dictionary to create a local map
		
		//create RMI registry
		
		Node bootstrapNode;
		//locate the booststrapping node, the node-0
		if(bootstrapNode == null) {
			//this is the first node being added
			
			//bind the service to the registry
			//TODO : How to do this
			try {
				Naming.bind();
			}catch (RemoteException e) {
				//log this in the client file
			}
			
			//create the bootstrap node
			
			//do what now ?
		}else {
			//existing node, so let us just allow this node to join the chord ring
			
			//TODO : complete this
		}
	}
	
}
