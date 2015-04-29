import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * Class containing the utility methods.
 * 
 * @author thirunavukarasu
 *
 */
public class Util {
	public static InetAddress getIP() throws IOException
    {
        MulticastSocket socket = new MulticastSocket(6800);
        socket.setReuseAddress(true);
        InetAddress group = InetAddress.getByName("230.0.1.1");
        socket.joinGroup(group);
        byte buff[] = new byte[8];
        DatagramPacket loopback = new DatagramPacket(buff, buff.length, group, 6800);

        /*Multicast and receive same packet*/
        socket.send(loopback);

        socket.receive(loopback);
        
        socket.leaveGroup(group);
        socket.disconnect();
        return loopback.getAddress();

    }
}
