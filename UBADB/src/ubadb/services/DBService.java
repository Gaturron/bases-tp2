package ubadb.services;

import ubadb.services.exceptions.DBServiceException;

public abstract class DBService
{
	//[start] Métodos abstractos
	public abstract void initializeService() 	throws DBServiceException;
	public abstract void startService()  		throws DBServiceException;
	public abstract void stopService()  		throws DBServiceException;
	public abstract void finalizeService()		throws DBServiceException;
	//[end]
	
	//[start] getName
	public String getName()
	{
		return this.getClass().getSimpleName();
	}
	//[end]
}
