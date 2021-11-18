package segmentedfilesystem.Packets;
import segmentedfilesystem.Packets.Packet;
import java.util.Arrays;

public class DataPacket extends Packet {
	protected byte[] data;
	protected int x;
	protected int y;
	protected int packetNumber;

	public DataPacket(byte[] bytes, int packetLength) {
		super(bytes);
		this.data = Arrays.copyOfRange(bytes, 4, packetLength );
		this.x = Byte.toUnsignedInt(bytes[2]);
		this.y = Byte.toUnsignedInt(bytes[3]);
		this.packetNumber = 256 * x + y;
	}

	public byte[] getData() {
		return data;
	}

	public int getPacketNumber() {
		return packetNumber;
	}

	public boolean isLastPacket() {
		return status % 4 == 3;
	}
}
