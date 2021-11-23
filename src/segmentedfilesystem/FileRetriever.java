package segmentedfilesystem;
import java.io.IOException;
import java.net.*;
public class FileRetriever {
	private InetAddress server;
	private int port;
	
	//Constructor for FileRetriever
	public FileRetriever(String server, int port) {
		//Save port number
                this.port = port;

                try {
			//Get IP address associated with the server name
                        this.server = InetAddress.getByName(server);
                } catch(UnknownHostException e) {
                        e.printStackTrace();
                }
        }

	public void downloadFiles() throws IOException {
		//Create instance of packetManager
		PacketManager packetManager = new PacketManager();
		// Create socket
		DatagramSocket socket = new DatagramSocket();

		try {
			//Create byte buffer
			byte[] buf = new byte[1028];
			//Create empty datagram packet to send to server
			DatagramPacket packet = new DatagramPacket(buf, buf.length, server, port);

			//Send empty packet to server
			//This tells the server to start sending files to us
			socket.send(packet);
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		//While loop to keep receiving packets from the server until we have received all the files
		while(!packetManager.haveAllFiles()){	
			byte[] data = new byte[1028];
			DatagramPacket packet = new DatagramPacket(data, 1028);
			socket.receive(packet);
			packetManager.addPacket(packet);
		}
		//Once while loop has completed, write all the files to disk
		packetManager.writeAllFiles();
	}
}	
