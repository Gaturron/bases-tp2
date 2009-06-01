package ubadb.services.exceptions;

import ubadb.exceptions.UBADBException;

/**
 * Exception
 * @author 	dcastro
 */
@SuppressWarnings("serial")
public class DBServiceException extends UBADBException 
{

	public DBServiceException() {
		super();
	}

	public DBServiceException(String arg0) {
		super(arg0);
	}

	public DBServiceException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public DBServiceException(Throwable arg0) {
		super(arg0);
	}

}
