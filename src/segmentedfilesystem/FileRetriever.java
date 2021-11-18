package segmentedfilesystem;
import java.io.IOException;
import java.net.*;
public class FileRetriever {
	private InetAddress server;
	private int port;

	public FileRetriever(String server, int port) {
                this.port = port;

                try {
                        this.server = InetAddress.getByName(server);
                } catch(UnknownHostException e) {
                        e.printStackTrace();
                }
        }

	public void downloadFiles() throws IOException {
		PacketManager packetManager = new PacketManager();
		DatagramSocket socket = new DatagramSocket();

		try {
			byte[] buf = new byte[1028];
			DatagramPacket packet = new DatagramPacket(buf, buf.length, server, port);
			socket.send(packet);
		} catch(IOException e) {
			e.printStackTrace();
		}
		
	
		while(!packetManager.haveAllFiles()){
			byte[] data = new byte[1028];
			DatagramPacket packet = new DatagramPacket(data, 1028);
			socket.receive(packet);
			packetManager.addPacket(packet);
		}
		packetManager.writeAllFiles();
	}
}	
