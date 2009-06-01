package ubadb.components.properties.exceptions;

import ubadb.components.exceptions.DBComponentException;

@SuppressWarnings("serial")
public class DBPropertiesException extends DBComponentException 
{

	public DBPropertiesException() {
		super();
	}

	public DBPropertiesException(String arg0) {
		super(arg0);
	}

	public DBPropertiesException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public DBPropertiesException(Throwable arg0) {
		super(arg0);
	}

}
