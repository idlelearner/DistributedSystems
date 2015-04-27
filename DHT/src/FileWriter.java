

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Filewriter is a asynchronous file writer which reads for the blocking queue
 * and writes to the server log file in disk.
 * 
 * @author dhass
 *
 */
public class FileWriter extends Thread {
	ArrayBlockingQueue<String> queue;
	Writer writer;

	public FileWriter(String filename, ArrayBlockingQueue<String> q) {
		queue = q;
		try {
			writer = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(filename), "utf-8"));
			writer.write("Logging started\n");
			// writer.close();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void run() {
		while (true) {
			String s;
			try {
				s = queue.take();
				if (s.contains("final client exit")) {
					System.out.println("Exiting!! from logger");
					writer.write(s);
					writer.write("\nExit encountered!");
					writer.flush();
					writer.close();
					break;
				}
				writer.write(s + "\n");
				// System.out.println("Logged : " + s);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}