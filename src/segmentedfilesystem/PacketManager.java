package segmentedfilesystem;

import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.HashMap;
import segmentedfilesystem.Packets.HeaderPacket;
import segmentedfilesystem.Packets.DataPacket;

public class PacketManager {
	//HashMap to be used to map packets to ReceivedFile objects
	protected HashMap<String, ReceivedFile> fileIdToFile;
	
	//Constructor for PacketManager
	public PacketManager() {
		//Initialize HashMap
		fileIdToFile = new HashMap<>();
	}
	
	//This method adds a packet to a ReceivedFile object
	public void addPacket(DatagramPacket packet) {
		//Get the status byte from the Datagram packet
		byte statusByte = packet.getData()[0];
		//Check to see if the packet is a header packet
		if(statusByte % 2 == 0) {
			//Create header packet
			HeaderPacket headerPacket = new HeaderPacket(packet.getData());
			
			//Get the fileId from the HeaderPacket
			String fileId = headerPacket.getFileId() + "";

			//Check if the fileId already has a ReceivedFile object created for it
			if(!fileIdToFile.containsKey(fileId)) {
				//If there is no ReceivedFile object associated with the fileId, create one
				ReceivedFile newFile = new ReceivedFile();
				//Add the header packet to the received file object
				newFile.addHeaderPacket(headerPacket);
				//Put the received file object in the hash map
				fileIdToFile.put(fileId, newFile);
			}
			else {
				//If there already is a received file object associated with the file id, add the header packet to the object
				fileIdToFile.get(fileId).addHeaderPacket(headerPacket);
			}
		}
		else {
			//Create dataPacket
			DataPacket dataPacket = new DataPacket(packet.getData(),packet.getLength());
			//Get fileId of the packet
			String fileId = dataPacket.getFileId() + "";
			//Same process as header packet but with data packet
			if(!fileIdToFile.containsKey(fileId)) {
				ReceivedFile newFile = new ReceivedFile();
				newFile.addDataPacket(dataPacket);
				fileIdToFile.put(fileId, newFile);
			}
			else {
				fileIdToFile.get(fileId).addDataPacket(dataPacket);
			}
		}
	}
	
	//Check to see if we have all the files
	public boolean haveAllFiles() {
		ArrayList<Boolean> allHere = new ArrayList<>();

		//Go through every file object in the HashMap
		for(ReceivedFile file : fileIdToFile.values()) {
			//Check if that file is complete
			if(file.isAllHere()) {
				allHere.add(true);
			}
			else {
				allHere.add(false);
			}
		}
		//Check if there are any incomplete files or if the HashMap is empty
		if(allHere.contains(false) || allHere.isEmpty()) {
			return false;
		}
		else  {
			return true;
		}

	}
	
	//Write files in the HashMap to disk
	public void writeAllFiles() {
		for(ReceivedFile file : fileIdToFile.values()) {
			file.writeToFile();
			System.out.println("Wrote " + file.fileName);
		}
	}
}
