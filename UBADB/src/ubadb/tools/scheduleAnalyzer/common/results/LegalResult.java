package ubadb.tools.scheduleAnalyzer.common.results;

public class LegalResult
{
	private boolean isLegal;
	private String 	illegalTransaction; // Cualquier transacción ilegal, si hubiera más de una
	private String 	message;	// Motivo por el cual es ilegal o cualquier mensaje adicional
	
	public LegalResult(boolean isLegal, String illegalTransaction, String message)
	{
		this.isLegal = isLegal;
		this.illegalTransaction = illegalTransaction;
		this.message = message;
	}

	public boolean isLegal()
	{
		return isLegal;
	}

	public String getIllegalTransaction()
	{
		return illegalTransaction;
	}

	public String getMessage()
	{
		return message;
	}

}
