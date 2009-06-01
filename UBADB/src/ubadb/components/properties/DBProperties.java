package ubadb.components.properties;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import ubadb.components.DBComponent;
import ubadb.components.properties.exceptions.DBPropertiesException;

/**
 *	Provee constantes de configuración de la BD 
 */
public class DBProperties extends DBComponent
{
	//[start] Atributos
	private Properties 	properties;
	private String	 	propertiesPath;
	//[end]
	
	//[start] Constructor
	public DBProperties()
	{
		propertiesPath = "config/ubadb.properties";
		properties = new Properties();
	}
	//[end]

	//[start] initializeComponent
	@Override
	public void initializeComponent() throws DBPropertiesException
	{
		try
		{
			properties.load(new FileReader(propertiesPath));
		}
		catch(FileNotFoundException e)
		{
			throw new DBPropertiesException("No se encuentra el archivo de configuración de propiedades '" + propertiesPath + "'",e);
		}
		catch(IOException e)
		{
			throw new DBPropertiesException("No pudo leerse el archivo de configuración de propiedades", e);
		}
	}
	//[end]
	
	//[start] finalizeComponent
	@Override
	public void finalizeComponent() throws DBPropertiesException
	{
		//Nada para hacer aquí
	}
	//[end]
	
	//[start] PROPIEDADES
	
	//[start] 	Recovery Manager
	public String RecoveryManagerLogFileName()
	{
		return properties.getProperty("RecoveryManager.logFileName","data/RecoveryManagerLog.dat");
	}
	
	public int RecoveryManagerMaxLogRecordsInMemory()
	{
		//Default = 25
		return Integer.parseInt(properties.getProperty("RecoveryManager.maxLogRecordsInMemory","25"));
	}
	//[end]
	
	//[end]
}
