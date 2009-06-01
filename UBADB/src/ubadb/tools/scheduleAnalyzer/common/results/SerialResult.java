package ubadb.tools.scheduleAnalyzer.common.results;

public class SerialResult
{
	private boolean isSerial;
	private String nonSerialTransaction; //Cualquier transacción no serial, si hubiera más de una
	private String message; // Cualquier mensaje adicional
	
	public SerialResult(boolean isSerial, String nonSerialTransaction, String message)
	{
		this.isSerial = isSerial;
		this.nonSerialTransaction = nonSerialTransaction;
		this.message = message;
	}

	public boolean isSerial()
	{
		return isSerial;
	}

	public String getNonSerialTransaction()
	{
		return nonSerialTransaction;
	}
	
	public String getMessage()
	{
		return message;
	}
}
