import java.io.Writer;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Looger to log to serverLogfile
 * 
 * @author dhass
 *
 */
public class ServerLogger {
	static private ServerLogger logger;
	private String filename;
	public Writer writer;
	ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<String>(10000);

	private ServerLogger(int serverId) {
		filename = "serverLogFile_" + serverId + ".txt";
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

	public static ServerLogger getInstance(int serverId) {
		if(logger == null) {
			logger = new ServerLogger(serverId);
		}
		return logger;
	}

	public synchronized void write(String s) {
		queue.add(s);
	}

}
