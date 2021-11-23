package segmentedfilesystem;
import java.io.FileOutputStream;
import java.util.SortedMap;
import java.util.TreeMap;
import segmentedfilesystem.Packets.DataPacket;
import segmentedfilesystem.Packets.HeaderPacket;

public class ReceivedFile {
	protected byte fileId;
	protected String fileName;
	protected SortedMap<Integer, byte[]> packets;
	protected boolean allHere;
	protected int finalPacketNumber;
	protected int counter;

	//Constructor
	public ReceivedFile(byte fileId) {
		this.fileId = fileId;
		packets = new TreeMap<>();
		allHere = false;
	}

	//Gets the file name from the header packet and sets
	//that value to this.fileName
	public void addHeaderPacket(HeaderPacket packet) {
		this.fileName = packet.getFileName();
		//Check if we have all the packets
		checkComplete();
	}

	public void addDataPacket(DataPacket packet) {
	       //Put the packet in the sorted map based on its packet number
	       packets.put(packet.getPacketNumber(), packet.getData());
	       //Increment the counter
	       counter++;
	       //Check if the packet is the last packet in terms of its packet number 
	       if(packet.isLastPacket()) {
		       //Set finalPacketNumber to packet number of the current packet
		       finalPacketNumber = packet.getPacketNumber();
	       }
	       //Check if we have all  the packets
	       checkComplete();
	}	       

	//Method to check if we have received all the packets
	private void checkComplete() {
		//We know when we have all the packets when counter - 1 == finalPacket number && fileName != null
		//ex: Suppose we receive 20 data packets, the final packet number would be 19.
		//So when we we have received all the data packets, counter would be 20 and finalPacketNumber would be 19.
		//We know we have received the header packet when the fileName has been set
		if(counter - 1 == finalPacketNumber && fileName != null) {
			allHere = true;
		}
	}

	public boolean isAllHere() {
		return allHere;
	}

	//Method to get the bytes in the sortedMap
	private byte[] getBytes() {
		//Create new byteArray of the size returned by getFileSize()
		byte[] fileBytes = new byte[getFileSize()];
		int index = 0;
		//For loop to go through every byte array in the sortedMap
		for(byte[] bytes : packets.values()) {
			//For every byte in the current byte array, add the current byte
			//to fileBytes at the current value of index.
			for(byte b: bytes) {
				fileBytes[index] = b;
				index++;
			}
		}
		return fileBytes;
	}

	//Method to get the file size
	private int getFileSize() {
		int size = 0;
		//Go through every byte array in the sorted map
		for(byte[] b : packets.values()) {
			//Get the length of the array and add it to size
			size += b.length;
		}
		return size;
	}

	public void writeToFile() {
		//Credit to Jeff and Biruk
		try {
			//Create the FileOutputStream and specify the path that it should write to
			FileOutputStream fos = new FileOutputStream(System.getProperty("user.dir") + "/" + fileName.trim());
			//Write bytes returned by getBytes() to the path.
			fos.write(getBytes());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
