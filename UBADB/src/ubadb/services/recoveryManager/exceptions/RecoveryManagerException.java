package ubadb.services.recoveryManager.exceptions;

import ubadb.services.exceptions.DBServiceException;

/**
 * Exception
 * @author 	dcastro
 */
@SuppressWarnings("serial")
public class RecoveryManagerException extends DBServiceException 
{

	public RecoveryManagerException() {
		super();
	}

	public RecoveryManagerException(String arg0) {
		super(arg0);
	}

	public RecoveryManagerException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public RecoveryManagerException(Throwable arg0) {
		super(arg0);
	}

}
