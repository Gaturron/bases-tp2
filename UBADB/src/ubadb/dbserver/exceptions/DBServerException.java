package ubadb.dbserver.exceptions;

import ubadb.exceptions.UBADBException;

@SuppressWarnings("serial")
public class DBServerException extends UBADBException 
{

	public DBServerException() {
		super();
	}

	public DBServerException(String arg0) {
		super(arg0);
	}

	public DBServerException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public DBServerException(Throwable arg0) {
		super(arg0);
	}

}
