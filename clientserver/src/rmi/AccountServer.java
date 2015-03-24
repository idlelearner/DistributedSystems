package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

//Interface that is implemented by the server class. Remote calls can be made on these functions
public interface AccountServer extends Remote {
	
	public int createAccount(String firstname, String lastname,String address) throws RemoteException;
	
	public String deposit(int acctID, int amt) throws RemoteException;
	
	public String withdraw(int acctID, int amt) throws RemoteException;
	
	public int getBalance(int acctID) throws RemoteException;
	
	public String transfer(int srcAcctID, int destAcctID, int amt) throws RemoteException;
	
	public void exitLogger() throws RemoteException;
}
