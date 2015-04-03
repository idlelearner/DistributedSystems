import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {

	protected Socket s;
	protected BankOperations bankOperations;
	protected ServerLogger log;

	public Server(Socket s, BankOperations bankOperations) {
		System.out.println("New client.");
		this.s = s;
		this.bankOperations = bankOperations;
		log = ServerLogger.getInstance();
	}

	public static void main(String[] args) throws IOException {

		if (args.length != 1)
			throw new RuntimeException("Syntax: EchoServer port-number");

		System.out.println("Starting on port " + args[0]);
		ServerSocket server = new ServerSocket(Integer.parseInt(args[0]));
		BankOperations bankOperations = new BankOperations();
		while (true) {
			System.out.println("Waiting for a client request");
			Socket client = server.accept();
			System.out.println("Received request from "
					+ client.getInetAddress());
			Server s = new Server(client, bankOperations);
			s.start();
		}
	}

	public void run() {
		try {
			InputStream istream = s.getInputStream();
			OutputStream ostream = s.getOutputStream();
			// new PrintStream (ostream).println
			// ("Welcome to the multithreaded echo server.");
			ObjectInputStream in = new ObjectInputStream(istream);
			ObjectOutputStream out = new ObjectOutputStream(ostream);
			try {
				while (true) {
					Request r = (Request) in.readObject();
					log.write("Transaction type : " + r.getTransactionType());
					log.write("Parameter received : " + r.params);
					if (r.getTransactionType().contains("exit"))
						break;
					// response.append(performOperation(r) + "\n");
					out.writeObject(performOperation(r));
				}
				// out.writeObject(response.toString());
				out.writeObject("exit");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			in.close();

			out.close();
			log.write("Client exit.");
			System.out.println("Client exit.");
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				s.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Calls the respective method to perform some operation
	 * 
	 * @param request
	 * @return
	 */
	public String performOperation(Request request) {
		StringBuilder status = new StringBuilder();
		Parameter param = request.getParams();
		switch (request.getTransactionType()) {
		case "createAcct":
			status.append(bankOperations.createAccount(param.getFirstname(),
					param.getLastname(), param.getAddress()));
			break;
		case "deposit":
			status.append(bankOperations.deposit(param.getAcctID(),
					param.getAmt()));
			break;
		case "withdraw":
			status.append(bankOperations.withdraw(param.getAcctID(),
					param.getAmt()));
			break;

		case "getBalance":
			status.append(bankOperations.getBalance(param.getAcctID()));
			break;
		case "transfer":
			status.append(bankOperations.transfer(param.getSrcAcctID(),
					param.getDestAcctID(), param.getAmt()));
			break;
		default:
			status.append("Operation not supported!");
			break;
		}
		log.write("Server Response :" + status.toString());
		return status.toString();
	}
}
