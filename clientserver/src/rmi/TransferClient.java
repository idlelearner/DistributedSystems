package rmi;

import java.rmi.RMISecurityManager;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//Test client for transfer operations
public class TransferClient extends Thread {

	int iterationCount;
	List<Integer> accts = new ArrayList<Integer>();
	String host;
	protected ClientLogger log;

	public TransferClient(String host, int itCount, List<Integer> accts) {
		this.iterationCount = itCount;
		this.accts = accts;
		this.host = host;
		this.log = ClientLogger.getInstance();
	}

	public void run() {
		try {

			System.setSecurityManager(new RMISecurityManager());
			AccountServer accServer = (AccountServer) Naming.lookup("//" + host
					+ "/AccountServer");

			// Do operations that we want TransferClient to do

			Random rnd = new Random();
			for (int i = 0; i < iterationCount; i++) {
				// Get random accts and perform transfer.
				int rndacctID1 = rnd.nextInt(accts.size());
				int rndacctID2 = rnd.nextInt(accts.size());
				transfer(accts.get(rndacctID1), accts.get(rndacctID2), 100,
						accServer);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void transfer(int acctID1, int acctID2, int amt,
			AccountServer accServer) throws RemoteException {
		log.write("Transferring " + amt + " from Acc " + acctID1 + " to Acc "
				+ acctID2);
		String status = (String) accServer.transfer(acctID1, acctID1, amt);
		log.write(status);
	}
}