package ubadb.tools.scheduleAnalyzer.common.results;

public class RecoverabilityResult
{
	private RecoverabilityType type;
	private String 	transaction1; // Un par de transacciones que tienen conflicto
	private String 	transaction2; 
	private String 	message;	// Motivo o cualquier mensaje adicional
	
	public RecoverabilityResult(RecoverabilityType type, String transaction1, String transaction2, String message)
	{
		this.type = type;
		this.transaction1 = transaction1;
		this.transaction2 = transaction2;
		this.message = message;
	}

	public RecoverabilityType getType()
	{
		return type;
	}

	public String getTransaction1()
	{
		return transaction1;
	}

	public String getTransaction2()
	{
		return transaction2;
	}

	public String getMessage()
	{
		return message;
	}
}
