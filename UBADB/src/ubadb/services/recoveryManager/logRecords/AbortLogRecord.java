package ubadb.services.recoveryManager.logRecords;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import ubadb.services.recoveryManager.exceptions.LogRecordException;

public class AbortLogRecord extends LogRecord
{
	public void serialize(DataOutputStream out) throws LogRecordException 
	{
		try
		{
			out.writeByte(2);
			out.writeInt(this.getTransactionId());
		}
		catch(Exception e)
		{
			throw new LogRecordException("Error al serializar un registro de 'Abort'",e);
		}
	}

	public AbortLogRecord(DataInputStream in) throws LogRecordException
	{
		try
		{
			this.transactionId = in.readInt();
		}
		catch(Exception e)
		{
			throw new LogRecordException("Error al deserializar un registro de 'Abort'",e);
		}
	}

	public AbortLogRecord(int transactionId) {
		this.transactionId = transactionId;
	}
}
