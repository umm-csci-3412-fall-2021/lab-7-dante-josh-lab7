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

	public ReceivedFile(byte fileId) {
		this.fileId = fileId;
		packets = new TreeMap<>();
		allHere = false;
	}

	public void addHeaderPacket(HeaderPacket packet) {
		//System.out.println("Adding a header packet");
		this.fileName = packet.getFileName();
		checkComplete();
	}

	public void addDataPacket(DataPacket packet) {
	     // System.out.println("Adding a data packet from received file class");
	       packets.put(packet.getPacketNumber(), packet.getData());
	       counter++;
	       if(packet.isLastPacket()) {
		       finalPacketNumber = packet.getPacketNumber();
	       }
	       checkComplete();
	}	       

	private void checkComplete() {
		if(counter - 1 == finalPacketNumber && fileName != null) {
			allHere = true;
		}
	}

	public boolean isAllHere() {
		return allHere;
	}

	private byte[] getBytes() {
		byte[] fileBytes = new byte[getFileSize()];
		int index = 0;
		for(byte[] bytes : packets.values()) {
			for(byte b: bytes) {
				fileBytes[index] = b;
				index++;
			}
		}
		return fileBytes;
	}

	private int getFileSize() {
		int size = 0;
		for(byte[] b : packets.values()) {
			size += b.length;
		}
		return size;
	}

	public void writeToFile() {
		//Credit to Jeff and Biruk
		System.out.println(fileName); 
		String path = System.getProperty("user.dir") + "/" + fileName;
		System.out.println(path);
		try {
			FileOutputStream fos = new FileOutputStream(System.getProperty("user.dir") + "/" + fileName.trim());
			fos.write(getBytes());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
