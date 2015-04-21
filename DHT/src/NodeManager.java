import java.rmi.Remote;
import java.rmi.RemoteException;

public interface NodeManager extends Remote {

	// Interface that is implemented by the server class. Remote calls can be
	// made on these functions

	public String findNode(String key) throws RemoteException;

	public String insert(String key, String value) throws RemoteException;

	public String lookup(String key) throws RemoteException;

}
