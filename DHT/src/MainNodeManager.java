import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MainNodeManager extends Remote {
	
	public Node join(String url) throws RemoteException;
	
	public void joinDone(String url) throws RemoteException;
	
	public String findNode(String key) throws RemoteException;

	public String insert(String key, String value) throws RemoteException;

	public String lookup(String key) throws RemoteException;
}
