package ubadb.services.recoveryManager.logRecords;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import ubadb.common.PageIdentifier;
import ubadb.services.recoveryManager.exceptions.LogRecordException;

public class UpdateLogRecord extends LogRecord
{
	private PageIdentifier pageId;
	private short length;
	private short offset;
	private byte[] beforeImage;
	private byte[] afterImage;

	public UpdateLogRecord(DataInputStream in) throws LogRecordException
	{
		try
		{
			this.transactionId = in.readInt();
			int tableId = in.readInt();
			int pageId = in.readInt();
			this.pageId = new PageIdentifier(tableId, pageId);
			this.length = in.readShort();
			this.offset = in.readShort();
	
			beforeImage = new byte[length];
			afterImage = new byte[length];
	
			in.read(beforeImage);
			in.read(afterImage);
		}
		catch(Exception e)
		{
			throw new LogRecordException("Error al deserializar un registro de 'Update'",e);
		}		
	}

	public void serialize(DataOutputStream out) throws LogRecordException 
	{
		try
		{
			out.writeByte(3);
			out.writeInt(this.getTransactionId());
			out.writeInt(this.getPageId().getTableId());
			out.writeInt(this.getPageId().getPageId());
			out.writeShort(this.getLength());
			out.writeShort(this.getOffset());
			out.write(this.getBeforeImage());
			out.write(this.getAfterImage());
		}
		catch(Exception e)
		{
			throw new LogRecordException("Error al serializar un registro de 'Update'",e);
		}
	}

	public UpdateLogRecord(int transactionId, PageIdentifier pageId, short length, short offset, byte[] beforeImage,
			byte[] afterImage) {
		this.transactionId = transactionId;
		this.pageId = pageId;
		this.length = length;
		this.offset = offset;
		this.beforeImage = beforeImage;
		this.afterImage = afterImage;
	}

	public int getTransactionId() {
		return transactionId;
	}

	public PageIdentifier getPageId() {
		return pageId;
	}

	public short getLength() {
		return length;
	}

	public short getOffset() {
		return offset;
	}

	public byte[] getBeforeImage() {
		return beforeImage;
	}

	public byte[] getAfterImage() {
		return afterImage;
	}
}