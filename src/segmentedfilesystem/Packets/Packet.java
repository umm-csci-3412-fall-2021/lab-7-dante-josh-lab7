package segmentedfilesystem.Packets;

public abstract class Packet {
	protected byte status;
	protected byte fileId;
	protected byte[] bytes;

	public Packet(byte[] bytes) {
		this.bytes = bytes;
		this.status = bytes[0];
		this.fileId = bytes[1];
	}

	public byte getFileId() {
		return fileId;
	}

	public byte getStatus() {
		return status;
	}
}
