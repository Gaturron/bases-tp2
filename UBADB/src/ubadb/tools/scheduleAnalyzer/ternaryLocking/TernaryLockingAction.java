package ubadb.tools.scheduleAnalyzer.ternaryLocking;

import ubadb.tools.scheduleAnalyzer.common.Action;

public class TernaryLockingAction extends Action
{
	//[start] Atributos
	private TernaryLockingActionType type;
	//[end]

	//[start] Constructores
	public TernaryLockingAction(TernaryLockingActionType type, String transaction, String item)
	{
		super(transaction,item);
		this.type = type;
	} 

	public TernaryLockingAction(TernaryLockingActionType type, String transaction)
	{
		super(transaction);
		this.type = type;
	}
	//[end]

	//[start] Getters & Setters
	public TernaryLockingActionType getType()
	{
		return type;
	}

	public void setType(TernaryLockingActionType type)
	{
		this.type = type;
	} 
	//[end]
	
	//[start] Métodos implementados
	@Override
	public boolean commits()
	{
		return type.equals(TernaryLockingActionType.COMMIT);
	}

	@Override
	public boolean reads()
	{
		return ( type.equals(TernaryLockingActionType.RLOCK) || type.equals(TernaryLockingActionType.WLOCK) );
	}

	@Override
	public boolean writes()
	{
		return type.equals(TernaryLockingActionType.WLOCK);
	}
	
	@Override
	public String toString(Boolean showTransaction)
	{
		String ret = "";
		
		if(showTransaction)
		{
			ret += transaction + ".";
		}
		
		if(type == TernaryLockingActionType.RLOCK)
		{
			ret = "RL(" + item + ")";
		}
		else if(type == TernaryLockingActionType.WLOCK)
		{
			ret = "WL(" + item + ")";
		}
		else if(type == TernaryLockingActionType.UNLOCK)
		{
			ret = "U(" + item + ")";
		}
		else if(type == TernaryLockingActionType.COMMIT)
		{
			ret = "Commit";
		}
		
		return ret;
	}
	//[end]
}
