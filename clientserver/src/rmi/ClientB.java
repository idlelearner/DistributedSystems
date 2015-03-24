package rmi;

import java.rmi.RMISecurityManager;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

//Second test client
public class ClientB {
	protected ClientLogger log = ClientLogger.getInstance();
	public static void main (String args[]) throws Exception{
		if(args.length != 4) {
			throw new RuntimeException ("Syntax ClientB <hostname>");
		}
		
		String hostname = args[0]+":"+args[1];
		System.setSecurityManager(new RMISecurityManager());
		AccountServer accServer = (AccountServer) Naming.lookup("//" + hostname + "/AccountServer");
		
		//Do operations that we want ClientB to do
		ClientB clt = new ClientB();
		
		int noOfAccts = 100;
		List<Integer> accts = clt.createAccts(noOfAccts, accServer);
		clt.deposit(accts, 100, accServer);
		System.out.println("Total Balance in all accounts : " + clt.getTotalBalance(accts, accServer));

		int threadCount = Integer.parseInt(args[2]);
		int iterationCount = Integer.parseInt(args[3]);
		List<TransferClient> tcList = new ArrayList<TransferClient>();
		for (int i = 0; i < threadCount; i++) {
			TransferClient tc = new TransferClient(hostname, iterationCount,
					accts);
			tc.start();
			tcList.add(tc);
		}

		// Wait till all other threads finish
		for (int i = 0; i < threadCount; i++)
			tcList.get(i).join();

		System.out.println("Total Balance in all accounts : " + clt.getTotalBalance(accts, accServer));
		
		System.out.println("ClientB exiting");
		clt.log.write("final client exit");
		//This is to stop the logger in server.
		accServer.exitLogger();
	}
	
	public List<Integer> createAccts(int noOfAccts, AccountServer accServer) throws RemoteException {
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

	public void deposit(List<Integer> accts, int amt, AccountServer accServer) throws RemoteException {
		for (int acct : accts) {
			String status = (String) accServer.deposit(acct, amt);
			System.out.println("For Account "+ acct + " Amount "+ amt + " : " + status);
		}
	}

	public double getTotalBalance(List<Integer> accts, AccountServer accServer) throws RemoteException {
		int total = 0;
		for (int acct : accts) {
			int balance = accServer.getBalance(acct);
			total = total + balance;
		}
		return total;
	}
}
