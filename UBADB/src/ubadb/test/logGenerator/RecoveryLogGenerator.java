package ubadb.test.logGenerator;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ubadb.common.PageIdentifier;
import ubadb.services.recoveryManager.logRecords.AbortLogRecord;
import ubadb.services.recoveryManager.logRecords.BeginLogRecord;
import ubadb.services.recoveryManager.logRecords.CommitLogRecord;
import ubadb.services.recoveryManager.logRecords.LogRecord;
import ubadb.services.recoveryManager.logRecords.UpdateLogRecord;

/**
 *	Sirve para generar logs de prueba 
 */
public class RecoveryLogGenerator
{
	public static void main(String[] args)
	{
		String outputLog = "out/RecoveryManagerLog.dat";
		
		List<LogRecord> records = generateRecords();
		
		saveToFile(records, outputLog);
	}

	private static List<LogRecord> generateRecords()
	{
		List<LogRecord> ret = new ArrayList<LogRecord>();
		
		//Acá va lo que uno quiere generar
		
//		ret.add(new BeginLogRecord(1));
//		ret.add(new BeginLogRecord(2));
//		
//		ret.add(new UpdateLogRecord(1,new PageIdentifier(10,20),(short)2,(short)0,new byte[]{0,0},new byte[]{1,1}));
//		
//		ret.add(new CommitLogRecord(1));
//		ret.add(new AbortLogRecord(2));
		
		return ret;
	}

	private static void saveToFile(List<LogRecord> records, String outputLog)
	{
		//Escribo el arreglo de bytes de cada record en el archivo de salida
		new File(outputLog).delete();
		try {
			DataOutputStream stream = new DataOutputStream(new FileOutputStream(outputLog));
			 for(LogRecord record : records){
				serialize(record, stream);
			}
			stream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void serialize(LogRecord record, DataOutputStream stream) throws IOException {
		if (record instanceof BeginLogRecord) {
			BeginLogRecord beginRecord = (BeginLogRecord) record;
			stream.writeByte(0);
			stream.writeInt(beginRecord.getTransactionId());
			return;
		}
		if (record instanceof CommitLogRecord) {
			CommitLogRecord commitRecord = (CommitLogRecord) record;
			stream.writeByte(1);
			stream.writeInt(commitRecord.getTransactionId());
			return;
		}
		if (record instanceof AbortLogRecord) {
			AbortLogRecord abortRecord = (AbortLogRecord) record;
			stream.writeByte(2);
			stream.writeInt(abortRecord.getTransactionId());
			return;
		}
		if (record instanceof UpdateLogRecord) {
			UpdateLogRecord updateRecord = (UpdateLogRecord) record;
			stream.writeByte(3);
			stream.writeInt(updateRecord.getTransactionId());
			stream.writeInt(updateRecord.getPageId().getTableId());
			stream.writeInt(updateRecord.getPageId().getPageId());
			stream.writeShort(updateRecord.getLength());
			stream.writeShort(updateRecord.getOffset());
			stream.write(updateRecord.getBeforeImage());
			stream.write(updateRecord.getAfterImage());
		}
	}
}
