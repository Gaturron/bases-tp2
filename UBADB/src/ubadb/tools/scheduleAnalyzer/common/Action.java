package ubadb.tools.scheduleAnalyzer.common;

public abstract class Action
{
	//[start] Atributos
	protected String transaction;
	protected String item;
	//[end]
	
	//[start] Constructores
	public Action(String transaction, String item)
	{
		this.transaction = transaction;
		this.item = item;
	}

	public Action(String transaction)
	{
		this.transaction = transaction;
	}
	//[end]

	//[start] Getters & Setters
	public String getTransaction()
	{
		return transaction;
	}

	public void setTransaction(String transaction)
	{
		this.transaction = transaction;
	}

	public String getItem()
	{
		return item;
	}

	public void setItem(String item)
	{
		this.item = item;
	}
	//[end]

	//[start] Métodos Abstractos
	public abstract boolean reads();
	public abstract boolean writes();
	public abstract boolean commits();
	
	public abstract String toString(Boolean showTransaction);
	//[end]
}
