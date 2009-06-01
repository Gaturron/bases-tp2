package ubadb.services.recoveryManager.logRecords;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import ubadb.services.recoveryManager.exceptions.LogRecordException;

public class BeginLogRecord extends LogRecord
{
	public void serialize(DataOutputStream out) throws LogRecordException
	{
		try
		{
			out.writeByte(0);
			out.writeInt(this.getTransactionId());
		}
		catch(Exception e)
		{
			throw new LogRecordException("Error al serializar un registro de 'Begin'",e);
		}
	}

	public BeginLogRecord(DataInputStream in) throws LogRecordException
	{
		try
		{
			this.transactionId = in.readInt();
		}
		catch(Exception e)
		{
			throw new LogRecordException("Error al deserializar un registro de 'Begin'",e);
		}
	}

	public BeginLogRecord(int transactionId) {
		this.transactionId = transactionId;
	}
}
