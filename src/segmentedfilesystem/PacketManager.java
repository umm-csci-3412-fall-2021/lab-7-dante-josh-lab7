package segmentedfilesystem;

import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.HashMap;
import segmentedfilesystem.Packets.HeaderPacket;
import segmentedfilesystem.Packets.DataPacket;

public class PacketManager {
	protected HashMap<String, ReceivedFile> fileIds;

	public PacketManager() {
		fileIds = new HashMap<>();
	}

	public void addPacket(DatagramPacket packet) {
		byte statusByte = packet.getData()[0];
		if(statusByte % 2 == 0) {
			HeaderPacket headerPacket = new HeaderPacket(packet.getData());
			String fileId = headerPacket.getFileId() + "";
			if(!fileIds.containsKey(fileId)) {
				ReceivedFile newFile = new ReceivedFile(headerPacket.getFileId());
				newFile.addHeaderPacket(headerPacket);
				//System.out.println("Adding a header packet from packet manager");
				fileIds.put(fileId, newFile);
			}
			else {
				fileIds.get(fileId).addHeaderPacket(headerPacket);
				//System.out.println("Adding a header packet from packet manager");
			}
		}
		else {
			DataPacket dataPacket = new DataPacket(packet.getData(),packet.getLength());
			String fileId = dataPacket.getFileId() + "";
			if(!fileIds.containsKey(fileId)) {
				ReceivedFile newFile = new ReceivedFile(dataPacket.getFileId());
				newFile.addDataPacket(dataPacket);
				fileIds.put(fileId, newFile);
				//System.out.println("Adding a data packet from packet manager");
			}
			else {
				fileIds.get(fileId).addDataPacket(dataPacket);
				//System.out.println("Adding a data packet from packet manager");
			}
		}
	}

	public boolean haveAllFiles() {
		//System.out.println("WTF");
		ArrayList<Boolean> allHere = new ArrayList<>();
		for(ReceivedFile file : fileIds.values()) {
			//System.out.println("Are you stuck here");
			if(file.isAllHere()) {
				allHere.add(true);
				//System.out.println("how about here?");
			}
			else {
				//System.out.println("Maybe here?");
				allHere.add(false);
			}
		}
		if(allHere.contains(false) || allHere.isEmpty()) {
			//System.out.println("maybe its here.");
			return false;
		}
		else  {
			//System.out.println("Better not be here.");
			return true;
		}

	}

	public void writeAllFiles() {
		for(ReceivedFile file : fileIds.values()) {
			file.writeToFile();
			System.out.println("Wrote a File");
		}
	}
}
