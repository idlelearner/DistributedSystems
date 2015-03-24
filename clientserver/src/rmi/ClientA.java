package rmi;

import java.rmi.RMISecurityManager;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

//First test client which executes a set of tasks on the remote server
public class ClientA {
	
	public static void main (String args[]) throws Exception{
		if(args.length != 2) {
			throw new RuntimeException ("Syntax ClientA <hostname> <port>");
		}
		
		System.setSecurityManager(new RMISecurityManager());
		String hostname = args[0]+":"+args[1];
		AccountServer accServer = (AccountServer) Naming.lookup("//" + hostname + "/AccountServer");
		
		//Do operations for ClientA
		ClientA clt = new ClientA();
		
		int noOfAccts = 2;
		
		List<Integer> accts = clt.createAccts(noOfAccts, accServer);
		
		int acctID1 = accts.get(0);
		int acctID2 = accts.get(1);

		int amt = 100;
		// Deposit
		clt.deposit(accts, 100, accServer);
		
		// Get balance
		clt.getBalance(accts, accServer);

		// Transfer amount
		clt.transfer(acctID1, acctID2, amt, accServer);

		// Get balance
		clt.getBalance(accts, accServer);
		
		// Withdraw amount.
		clt.withdraw(accts, amt, accServer);
		
		//Transfer again
		clt.transfer(acctID1, acctID2, amt, accServer);
		
		// Get balance
		clt.getBalance(accts, accServer);
	
		System.out.println ("ClientA exiting");
		//This is to stop the logger in server.
		accServer.exitLogger();
	}
	
	public List<Integer> createAccts(int noOfAccts, AccountServer accServer) throws RemoteException{
		int i = 1;
		List<Integer> accts = new ArrayList<Integer>();
		while (i <= noOfAccts) {
			int acctID = accServer.createAccount("F"+i, "L"+i, "A"+i);
			System.out.println("Account with account id : " + acctID + " created on server");
			accts.add(acctID);
			i++;
		}
		return accts;
	}
	
	public void deposit(List<Integer> accts, int amt, AccountServer accServer) throws RemoteException{
		for (int acct : accts) {
			String status = accServer.deposit(acct, amt);
			System.out.println("For Account "+ acct + " Amount "+ amt + " : " + status);
		}
	}
	
	public void getBalance(List<Integer> accts, AccountServer accServer) throws RemoteException{
		for (int acct : accts) {
			int balance = accServer.getBalance(acct);
			System.out.println("For Account "+ acct + " Balance : " + balance);
		}
	}
	
	public void withdraw(List<Integer> accts, int amt, AccountServer accServer) throws RemoteException{
		for (int acct : accts) {
			String status = accServer.withdraw(acct, amt);
			System.out.println("Withdrawing " + amt + " for Acc " + acct + " : " + status);
		}
	}
	
	public void transfer(int acctID1, int acctID2, int amt, AccountServer accServer) throws RemoteException{
		System.out.println("Transferring " + amt + " from Acc " + acctID1 + " to Acc " + acctID2);
		String status = accServer.transfer(acctID1, acctID2, amt);
		System.out.println(status);
	}
}
