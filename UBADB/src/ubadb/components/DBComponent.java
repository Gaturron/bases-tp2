package ubadb.components;

import ubadb.components.exceptions.DBComponentException;

public abstract class DBComponent
{
	//[start] Métodos abstractos
	public abstract void initializeComponent() 	throws DBComponentException;
	public abstract void finalizeComponent()	throws DBComponentException;
	//[end]
	
	//[start] getName
	public String getName()
	{
		return this.getClass().getSimpleName();
	}
	//[end]
}
