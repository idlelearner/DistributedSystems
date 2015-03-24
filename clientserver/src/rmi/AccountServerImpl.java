package rmi;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.Naming;
import java.rmi.registry.*;

//Implementation of the server class. Functions defined in the AccountServer interface implemented
public class AccountServerImpl extends UnicastRemoteObject implements AccountServer {
	
	protected BankOperations bankOperations;
	protected ServerLogger log;
	
	public AccountServerImpl() throws RemoteException {
		super();
		bankOperations = new BankOperations();
		log = ServerLogger.getInstance();
	}
	
	public static void main (String args[]) throws Exception {
		System.setSecurityManager (new RMISecurityManager());
		
		AccountServerImpl accountServerImpl = new AccountServerImpl();
		
		if(args.length == 0){
			//No port is given, then assume that it is the standard port 1099
			Naming.bind("AccountServer", accountServerImpl);
		}
		else {
				// rmiregistry is on port specified in args[0]. Bind to that registry.
		       Registry localRegistry = LocateRegistry.getRegistry( Integer.parseInt( args[0] ));
		       localRegistry.bind ("AccountServer", accountServerImpl);
		}
		
		System.out.println("Server Up!");
	}
	
	//Implement the functions defined in the interface
	
	public int createAccount(String firstname, String lastname,String address) throws RemoteException {
		log.write("Transaction type : Create Account");
		log.write("Parameters : "+ firstname + ", " + lastname + ", " + address);
		int createdAcc = bankOperations.createAccount(firstname, lastname, address);
		log.write("Account " + createdAcc + " created successfully");
		return createdAcc;
		
	}
	
	public String deposit(int acctID, int amt) throws RemoteException {
		log.write("Transaction type : Deposit");
		log.write("Parameters : "+ acctID + ", " + amt);
		String status = bankOperations.deposit(acctID, amt);
		log.write("Status after depost : " + status);
		return status;
	}
	
	public String withdraw(int acctID, int amt) throws RemoteException {
		log.write("Transaction type : Withdraw");
		log.write("Parameters : "+ acctID + ", " + amt);
		String status = bankOperations.withdraw(acctID, amt);
		return status;
	}
	
	public int getBalance(int acctID) throws RemoteException {
		log.write("Transaction type : Get Balance");
		log.write("Parameters : "+ acctID);
		int balance = bankOperations.getBalance(acctID); 
		log.write("Balance is : " + balance);
		return balance;
	}
	
	public String transfer(int srcAcctID, int destAcctID, int amt) throws RemoteException {
		log.write("Transaction type : Transfer between accounts");
		log.write("Parameters : "+ srcAcctID + ", " + destAcctID + ", " + amt);
		String status = bankOperations.transfer(srcAcctID, destAcctID, amt);
		log.write("Status after transfer : "+ status);
		return status;
	}

	public void exitLogger() throws RemoteException{
		log.write("final client exit");
	}
}
