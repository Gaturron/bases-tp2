package ubadb.services.recoveryManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ubadb.common.PageIdentifier;
import ubadb.dbserver.DBServer;
import ubadb.logger.DBLogger;
import ubadb.services.DBService;
import ubadb.services.exceptions.DBServiceException;
import ubadb.services.recoveryManager.exceptions.RecoveryManagerException;
import ubadb.services.recoveryManager.logRecords.AbortLogRecord;
import ubadb.services.recoveryManager.logRecords.CommitLogRecord;
import ubadb.services.recoveryManager.logRecords.LogRecord;
import ubadb.services.recoveryManager.logRecords.UpdateLogRecord;

/**
 *	Módulo de "Recuperación ante fallas" 
 */
public class RecoveryManager extends DBService
{
	//[start] Atributos
	private int	 			maxLogRecordsInMemory;
	private List<LogRecord> logRecordsInMemory;
	private String 			logFileName;
	//[end]
	
	//[start] Constructor
	public RecoveryManager()
	{
		maxLogRecordsInMemory = DBServer.getDBProperties().RecoveryManagerMaxLogRecordsInMemory();
		logFileName = DBServer.getDBProperties().RecoveryManagerLogFileName();
		logRecordsInMemory = new ArrayList<LogRecord>();
	}
	//[end]
	
	//[start] Métodos públicos
	
	//[start] 	addLogRecord
	/**
	 *	Agrega un registro al Log.
	 *	Si supera la capacidad de registros de log en memoria, se hace un flush y se agrega el record a memoria.
	 *	Si no, guarda el registro en memoria 
	 */
	public void addLogRecord(LogRecord logRecord) throws RecoveryManagerException
	{
		if (logRecordsInMemory.size() > maxLogRecordsInMemory) 
			flushLog();

		logRecordsInMemory.add(logRecord);
	}
	//[end]

	//[start] 	flushLog
	/**
	 * Guarda en disco los registros que hay en memoria y vacía la lista de registros en memoria
	 */
	public void flushLog() throws RecoveryManagerException
	{
		//Grabo en el disco el log
		saveLogToFile(logRecordsInMemory, logFileName);
		
		//Limpio los registros del log
		logRecordsInMemory.clear();
	}
	//[end]
	
	//[start] 	recoverFromCrash
	/**
	 *	Implementa el algoritmo de recuperación UNDO/REDO sin checkpoints 
	 */
	public void recoverFromCrash() throws RecoveryManagerException
	{
		//Sigo los pasos del algoritmo de recuperación UNDO/REDO sin checkpointing

		Map<Integer, List<LogRecord>> transactionRecords = new HashMap<Integer, List<LogRecord>>();
		List<Integer> unfinishedTransactionIds = new ArrayList<Integer>();
		List<Integer> abortedTransactionIds = new ArrayList<Integer>();
		List<Integer> committedTransactionIds = new ArrayList<Integer>();

		//Levanto el log y cargo un map con las transacciones, las incompletas, abortadas y con commit 
		analyzeLog(transactionRecords, unfinishedTransactionIds, abortedTransactionIds, committedTransactionIds);

		//TODO: Falta completar que se hagan las que tienen commit y que se deshagan las demás
		
		//Escribo el abort de las incompletas
		writeAbort(unfinishedTransactionIds);
	}
	//[end]
	
	//[end]

	//[start] Métodos privados
	
	//[start] saveLogToFile
	private static void saveLogToFile(List<LogRecord> records, String logFileName) throws RecoveryManagerException
	{
		try 
		{
			//Creo archivo de salida
			DataOutputStream stream = new DataOutputStream(new FileOutputStream(logFileName, true));	//append = true

			//Serializo cada registro del log
			for (LogRecord record : records) 
				record.serialize(stream);
			
			//Cierro el archivo
			stream.flush();
			stream.close();
		} 
		catch (Exception e) 
		{
			throw new RecoveryManagerException("Error al guardar el log en disco",e);
		}
	}
	//[end]
	
	//[start] readLogFromFile
	private void readLogFromFile(List<LogRecord> records) throws RecoveryManagerException
	{
		try 
		{
			DataInputStream in = new DataInputStream(new FileInputStream(logFileName));
			
			while (in.available() > 0)
				records.add(LogRecord.deserialize(in));
		} 
		catch (FileNotFoundException e) 
		{
			throw new RecoveryManagerException("No existe el archivo '" + logFileName + "'",e);
		}
		catch(Exception e) 
		{
			throw new RecoveryManagerException("Error al leer el log del disco",e);
		}
	}
	//[end]
	
	//[start] 	analyzeLog
	/**
	 *	Recorre el log del disco, armando las 3 listas más el diccionario con las acciones de cada transacción 
	 */
	private void analyzeLog(Map<Integer, List<LogRecord>> transactionRecords, List<Integer> unfinishedTransactionIds, List<Integer> abortedTransactionIds, List<Integer> committedTransactionIds) throws RecoveryManagerException
	{
		List<LogRecord> logRecordsFromDisk = new ArrayList<LogRecord>(); 

		//Levanto el log del disco
		readLogFromFile(logRecordsFromDisk);

		//Recorro los registros, armando cada transacción por separado
		for(LogRecord record : logRecordsFromDisk)
		{
			int idTrans = record.getTransactionId();
			
			//Si no estaba, la agrego al map con una lista vacía
			if (!transactionRecords.containsKey(idTrans)) 
				transactionRecords.put(idTrans, new ArrayList<LogRecord>());
			
			//Agrego el registro a la lista de la transacción
			transactionRecords.get(idTrans).add(record);
		}
		
		//Completo la lista de incompletas, con commit y abortadas
		for(Entry<Integer, List<LogRecord>> transactionEntry : transactionRecords.entrySet())
		{
			List<LogRecord> recordsFromTransaction = transactionEntry.getValue();
			
			//Si el último es commit...
			if(recordsFromTransaction.get(recordsFromTransaction.size()-1) instanceof CommitLogRecord)
				committedTransactionIds.add(transactionEntry.getKey());
			//Si es un abort...
			else if(recordsFromTransaction.get(recordsFromTransaction.size()-1) instanceof AbortLogRecord)
				abortedTransactionIds.add(transactionEntry.getKey());
			else
				unfinishedTransactionIds.add(transactionEntry.getKey());
		}
	}
	//[end]
	
	//[start] 	redoTransaction
	/**
	 *	 Rehace una transacción (por ahora, el único record que se debe rehacer es el UpdateLogRecord)
	 *	 IMPORTANTE: no hace nada con el commit
	 */
	private void redoTransaction(List<LogRecord> logRecords)
	{
		//Rehace cada paso de la transacción (debe usar el updatePage con la imagen nueva)
		for(LogRecord logRecord : logRecords)
		{
			if(logRecord instanceof UpdateLogRecord)
			{
				UpdateLogRecord updateRecord = (UpdateLogRecord) logRecord;
				updatePage(updateRecord.getTransactionId(), updateRecord.getPageId(), updateRecord.getLength(), updateRecord.getOffset(), updateRecord
						.getAfterImage());
			}
		}
	}
	//[end]
	
	//[start] 	undoTransaction
	/**
	 * Deshace una transacción (por ahora, el único record que se debe deshacer es el UpdateLogRecord)
	 * IMPORTANTE: no hace nada con el abort
	 */
	private void undoTransaction(List<LogRecord> logRecords)
	{
		// Deshace cada paso de la transacción (debe usar el updatePage con la imagen anterior)

		for(LogRecord logRecord : logRecords)
		{
			if(logRecord instanceof UpdateLogRecord)
			{
				UpdateLogRecord updateRecord = (UpdateLogRecord) logRecord;
				updatePage(updateRecord.getTransactionId(), updateRecord.getPageId(), updateRecord.getLength(), updateRecord.getOffset(), updateRecord.getBeforeImage());
			}
		}
	}
	//[end]

	//[start] 	updatePage
	/**
	 * Actualiza la página indicada en disco
	 */
	private void updatePage(int transactionId, PageIdentifier pageId, short lenght, short offset, byte[] image)
	{
		//Esta llamada quedará para completar cuando se vayan agregando diferentes módulos al sistema
		
		// Esta llamada quedará para completar cuando se vayan agregando diferentes módulos al
		// sistema
		// Por ahora lo unico que hacemos es tirar al log asi vemos que onda
		DBLogger.info("updatePage: trans " + transactionId + ", tabla " + pageId.getTableId() + ", pagina "
						+ pageId.getPageId() + ", length " + lenght + ", offset " + offset+", image ["+image[0]+","+image[0]+"] ");
	}
	//[end]
	
	//[start] 	writeAbort
	/**
	 * Escribe en el log un Abort de las transacciones que no terminaron.
	 * 
	 * @param unfinishedTransactionIds
	 * @throws RecoveryManagerException
	 */
	private void writeAbort(List<Integer> unfinishedTransactionIds) throws RecoveryManagerException 
	{
		for(Integer unfinishedTransactionId : unfinishedTransactionIds)
		{
			this.addLogRecord(new AbortLogRecord(unfinishedTransactionId));
			flushLog();
		}
	}
	//[end]
	
	//[end]
	
	
	//[start] Métodos abstractos
	
	//[start] 	initializeService
	@Override
	public void initializeService() throws DBServiceException
	{
		//Nada para hacer aquí...
	}
	//[end]
	
	//[start]	finalizeService
	@Override
	public void finalizeService() throws DBServiceException
	{
		//Nada para hacer aquí...		
	}
	//[end]

	//[start]	startService
	@Override
	public void startService() throws DBServiceException
	{
		//Nada para hacer aquí...		
	}
	//[end]

	//[start]	stopService
	@Override
	public void stopService() throws DBServiceException
	{
		//Nada para hacer aquí...		
	}
	//[end]

	//[end]
}
