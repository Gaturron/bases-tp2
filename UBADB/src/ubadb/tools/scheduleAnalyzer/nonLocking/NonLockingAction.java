package ubadb.tools.scheduleAnalyzer.nonLocking;

import ubadb.tools.scheduleAnalyzer.common.Action;

public class NonLockingAction extends Action
{
	//[start] Atributos
	private NonLockingActionType type;
	//[end]

	//[start] Constructores
	public NonLockingAction(NonLockingActionType type, String transaction, String item)
	{
		super(transaction,item);
		this.type = type;
	}

	public NonLockingAction(NonLockingActionType type, String transaction)
	{
		super(transaction);
		this.type = type;
	}
	//[end]

	//[start] Getters & Setters
	public NonLockingActionType getType()
	{
		return type;
	}

	public void setType(NonLockingActionType type)
	{
		this.type = type;
	} 
	//[end]
	
	//[start] Métodos implementados
	@Override
	public boolean commits()
	{
		return type.equals(NonLockingActionType.COMMIT);
	}

	@Override
	public boolean reads()
	{
		return type.equals(NonLockingActionType.READ);
	}

	@Override
	public boolean writes()
	{
		return type.equals(NonLockingActionType.WRITE);
	}
	
	@Override
	public String toString(Boolean showTransaction)
	{
		String ret = "";
		
		if(showTransaction)
		{
			ret += transaction + ".";
		}
		
		if(type == NonLockingActionType.READ)
		{
			ret = "R(" + item + ")";
		}
		else if(type == NonLockingActionType.WRITE)
		{
			ret = "W(" + item + ")";
		}
		else if(type == NonLockingActionType.COMMIT)
		{
			ret = "Commit";
		}
		
		return ret;
	}
	//[end]
}
