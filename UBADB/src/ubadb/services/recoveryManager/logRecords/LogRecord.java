package ubadb.services.recoveryManager.logRecords;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import ubadb.services.recoveryManager.exceptions.LogRecordException;

public abstract class LogRecord
{
	protected int transactionId;

	public int getTransactionId() 
	{
		return transactionId;
	}

	public abstract void serialize(DataOutputStream out) throws LogRecordException;
	
	public static LogRecord deserialize(DataInputStream in) throws LogRecordException
	{
		try
		{
			byte tipo = in.readByte();
	
			LogRecord record;
	
			switch (tipo) 
			{
				case (0):
					record = new BeginLogRecord(in);
					break;
				case (1):
					record = new CommitLogRecord(in);
					break;
				case (2):
					record = new AbortLogRecord(in);
					break;
				case (3):
					record = new UpdateLogRecord(in);
					break;
				default:
					throw new LogRecordException("Tipo de registro de log desconocido");
			}
	
			return record;
		}
		catch(Exception e)
		{
			throw new LogRecordException("Error al leer registro del log",e);
		}
	}
}
