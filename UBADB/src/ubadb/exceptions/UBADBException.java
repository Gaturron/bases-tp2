package ubadb.exceptions;

/**
 * General UBADB Exception
 * @author 	dcastro
 */
@SuppressWarnings("serial")
public class UBADBException extends Exception 
{

	public UBADBException() {
		super();
	}

	public UBADBException(String arg0) {
		super(arg0);
	}

	public UBADBException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public UBADBException(Throwable arg0) {
		super(arg0);
	}

}
