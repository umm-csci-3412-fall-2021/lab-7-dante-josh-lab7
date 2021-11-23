package segmentedfilesystem.Packets;
import segmentedfilesystem.Packets.Packet;
import java.util.Arrays;

public class HeaderPacket extends Packet {
	protected String fileName;

	public HeaderPacket(byte[] bytes) {
		super(bytes);
		this.fileName = new String(Arrays.copyOfRange(bytes, 2, bytes.length-1));
	}

	public String getFileName() {
		return fileName;
	}
}
