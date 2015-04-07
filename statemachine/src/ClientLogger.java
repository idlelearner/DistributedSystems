import java.io.Writer;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Looger to log to serverLogfile
 * 
 * @author dhass
 *
 */
public class ClientLogger {
	static private ClientLogger logger = new ClientLogger();
	private String filename;
	public Writer writer;
	ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<String>(10000);

	private ClientLogger() {
		filename = "clientLogFile.txt";
		try {
			FileWriter fw = new FileWriter(filename, queue);
			fw.start();
		} finally {
			try {
				writer.close();
			} catch (Exception ex) {
			}
		}
	}

	public static ClientLogger getInstance() {
		return logger;
	}

	public synchronized void write(String s) {
		queue.add(s);
	}

}
