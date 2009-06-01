package ubadb.services.recoveryManager.exceptions;

import ubadb.services.exceptions.DBServiceException;

/**
 * Exception
 * @author 	dcastro
 */
@SuppressWarnings("serial")
public class LogRecordException extends DBServiceException 
{

	public LogRecordException() {
		super();
	}

	public LogRecordException(String arg0) {
		super(arg0);
	}

	public LogRecordException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public LogRecordException(Throwable arg0) {
		super(arg0);
	}

}
