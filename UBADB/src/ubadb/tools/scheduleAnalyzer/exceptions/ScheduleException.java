package ubadb.tools.scheduleAnalyzer.exceptions;


@SuppressWarnings("serial")
public class ScheduleException extends Exception
{
	public ScheduleException()
	{
		super();
	}

	public ScheduleException( String message )
	{
		super(message);
	}

	public ScheduleException( String message, Throwable cause )
	{
		super(message, cause);
	}

	public ScheduleException( Throwable cause )
	{
		super(cause);
	}
}
