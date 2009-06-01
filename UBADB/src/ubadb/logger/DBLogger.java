package ubadb.logger;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 *	Clase estática que loguea diferentes eventos en un archivo en disco
 */
public class DBLogger
{
	//[start] Atributos
	private static Logger logger;
	//[end]
	
	//[start] Constructor estático
	static
	{
		try
		{
			logger = Logger.getRootLogger();
			
			PropertyConfigurator.configure("config/log4j.properties");
		}
		catch(Exception e)
		{
			System.err.println("No puede inicializarse el Logger");
			e.printStackTrace();
		}
	}
	//[end]

	//[start] Debug
	public static void debug(String message)
	{
		logger.debug(message);
	}
	public static void debug(String message, Throwable t)
	{
		logger.debug(message,t);
	}
	//[end]
	
	//[start] Info
	public static void info(String message)
	{
		logger.info(message);
	}
	public static void info(String message, Throwable t)
	{
		logger.info(message,t);
	}
	//[end]
	
	//[start] Warn
	public static void warn(String message)
	{
		logger.warn(message);
	}
	public static void warn(String message, Throwable t)
	{
		logger.warn(message,t);
	}
	//[end]
	
	//[start] Error
	public static void error(String message)
	{
		logger.error(message);
	}
	public static void error(String message, Throwable t)
	{
		logger.error(message,t);
	}
	//[end]
	
	//[start] Fatal
	public static void fatal(String message)
	{
		logger.fatal(message);
	}
	public static void fatal(String message, Throwable t)
	{
		logger.fatal(message,t);
	}
	//[end]
}
