package ubadb.tools.scheduleAnalyzer.binaryLocking;

import ubadb.tools.scheduleAnalyzer.common.Action;

public class BinaryLockingAction extends Action
{
	//[start] Atributos
	private BinaryLockingActionType type;
	//[end]

	//[start] Constructores
	public BinaryLockingAction(BinaryLockingActionType type, String transaction, String item)
	{
		super(transaction,item);
		this.type = type;
	} 

	public BinaryLockingAction(BinaryLockingActionType type, String transaction)
	{
		super(transaction);
		this.type = type;
	}
	//[end]

	//[start] Getters & Setters
	public BinaryLockingActionType getType()
	{
		return type;
	}

	public void setType(BinaryLockingActionType type)
	{
		this.type = type;
	} 
	//[end]

	//[start] Métodos implementados
	@Override
	public boolean commits()
	{
		return type.equals(BinaryLockingActionType.COMMIT);
	}

	@Override
	public boolean reads()
	{
		return type.equals(BinaryLockingActionType.LOCK);
	}

	@Override
	public boolean writes()
	{
		return type.equals(BinaryLockingActionType.LOCK);
	}
	
	@Override
	public String toString(Boolean showTransaction)
	{
		String ret = "";
		
		if(showTransaction)
		{
			ret += transaction + ".";
		}
		
		if(type == BinaryLockingActionType.LOCK)
		{
			ret = "L(" + item + ")";
		}
		else if(type == BinaryLockingActionType.UNLOCK)
		{
			ret = "U(" + item + ")";
		}
		else if(type == BinaryLockingActionType.COMMIT)
		{
			ret = "Commit";
		}
		
		return ret;
	}
	//[end]
}
