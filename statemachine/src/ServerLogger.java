import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Looger to log to serverLogfile
 * 
 * @author dhass
 *
 */
public class ServerLogger {
	static private ServerLogger logger = new ServerLogger();
	private String filename;
	public Writer writer;
	ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<String>(10000);

	private ServerLogger() {
		filename = "serverLogFile.txt";
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

	public static ServerLogger getInstance() {
		return logger;
	}

	public synchronized void write(String s) {
		queue.add(s);
	}

}
