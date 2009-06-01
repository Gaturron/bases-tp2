package ubadb.dbserver;

import java.util.Map;

import ubadb.components.DBComponent;
import ubadb.components.DBComponentsEnum;
import ubadb.components.exceptions.DBComponentException;
import ubadb.components.properties.DBProperties;
import ubadb.dbserver.exceptions.DBServerException;
import ubadb.logger.DBLogger;
import ubadb.services.DBService;
import ubadb.services.DBServicesEnum;
import ubadb.services.exceptions.DBServiceException;
import ubadb.services.recoveryManager.RecoveryManager;
import ubadb.services.recoveryManager.exceptions.RecoveryManagerException;

/**
 *	Clase estática que representa al Servidor de BD 
 */
public class DBServer
{
	//[start] Atributos
	private static Map<DBServicesEnum,DBService> 		dbServicesMap;
	private static Map<DBComponentsEnum,DBComponent> 	dbComponentsMap;
	//[end]
	
	//[start] Start
	
	//[start] 	startDBServer
	public static void startDBServer() throws DBServerException
	{
		//Obtengo los componentes y los inicializo...
		dbComponentsMap = DBFactory.getComponents();
		initializeComponents();
		
		//Ahora hago lo mismo con los servicios... (OJO: Es necesario tener algunos componentes inicializados para ejecutar el getServices!)
		//Además, los arranco
		dbServicesMap 	= DBFactory.getServices();
		initializeServices();
		startServices();
		
		//TODO: En el futuro aquí tal vez haga más cosas
		
		//Le pido al Recovery Manager que se fije si no hay que recuperarse de una falla anterior
		recoverFromPreviousCrash();
	}
	//[end]

	//[start] 	Métodos privados
	
	//[start]		initializeComponents
	private static void initializeComponents() throws DBServerException
	{
		try
		{
			for(DBComponent dbComponent : dbComponentsMap.values())
			{
				dbComponent.initializeComponent();
				DBLogger.info(dbComponent.getName() + " inicializado");
			}
		}
		catch(DBComponentException e)
		{
			throw new DBServerException("No pudo inicializarse un componente, el servidor se cerrará",e);
		}
	}
	//[end]
	
	//[start]		initializeServices
	private static void initializeServices() throws DBServerException
	{
		try
		{
			for(DBService dbService : dbServicesMap.values())
			{
				dbService.initializeService();
				DBLogger.info(dbService.getName() + " inicializado");
			}
		}
		catch(DBServiceException e)
		{
			throw new DBServerException("No pudo inicializarse un servicio, el servidor se cerrará",e);
		}
	}
	//[end]
	
	//[start]		startServices
	private static void startServices() throws DBServerException
	{
		try
		{
			for(DBService dbService : dbServicesMap.values())
			{
				dbService.startService();
				DBLogger.info(dbService.getName() + " arrancado");
			}
		}
		catch(DBServiceException e)
		{
			throw new DBServerException("No pudo arrancarse un servicio, el servidor se cerrará",e);
		}
		
	}
	//[end]
	
	//[start] 		recoverFromPreviousCrash
	private static void recoverFromPreviousCrash() throws DBServerException
	{
		RecoveryManager recoveryManager = (RecoveryManager) getService(DBServicesEnum.RECOVERY_MANAGER);
		
		if(recoveryManager != null)
		{
			try
			{
				recoveryManager.recoverFromCrash();
			}
			catch(RecoveryManagerException e)
			{
				throw new DBServerException("No pudo recuperarse de fallas previas",e);
			}
		}
	}
	//[end]
	
	//[end]
	
	//[end]
	
	//[start] Exit

	//[start] 	exitDBServer
	public static void exitDBServer() throws DBServerException
	{
		//Paro los servicios
		stopServices();
		
		//Termino los servicios
		finalizeServices();
		
		//Termino los componentes
		finalizeComponents();
	}
	//[end]
	
	//[start] 	Métodos privados
	
	//[start]		stopServices
	private static void stopServices()
	{
		for(DBService dbService : dbServicesMap.values())
		{
			try
			{
				dbService.stopService();
				DBLogger.info(dbService.getName() + " frenado");
			}
			catch(DBServiceException e)
			{
				//Si no puede pararse un servicio, no puedo hacer nada, sigo con los demás
				DBLogger.info(dbService.getName() + " no pudo frenarse");
			}
		}
	}
	//[end]

	//[start]		finalizeServices
	private static void finalizeServices()
	{
		for(DBService dbService : dbServicesMap.values())
		{
			try
			{
				dbService.finalizeService();
				DBLogger.info(dbService.getName() + " finalizado");
			}
			catch(DBServiceException e)
			{
				//Si no puede terminarse un servicio, no puedo hacer nada, sigo con los demás
				DBLogger.info(dbService.getName() + " no pudo finalizarse");
			}
		}
	}
	//[end]
	
	//[start]		finalizeComponents
	private static void finalizeComponents()
	{
		for(DBComponent dbComponent: dbComponentsMap.values())
		{
			try
			{
				dbComponent.finalizeComponent();
				DBLogger.info(dbComponent.getName() + " finalizado");
			}
			catch(DBComponentException e)
			{
				//Si no puede terminarse un componente, no puedo hacer nada, sigo con los demás
				DBLogger.info(dbComponent.getName() + " no pudo finalizarse");
			}
		}
	}
	//[end]
	
	//[end]
	
	//[end]
	
	//[start] getService
	/**
	 * Devuelve al componente solicitado (puede devolver null si el servicio no está configurado)
	 */
	public static DBService getService(DBServicesEnum dbServiceEnum)
	{
		return dbServicesMap.get(dbServiceEnum);
	}
	//[end]
	
	//[start] getComponent
	/**
	 * Devuelve al componente solicitado (NUNCA devuelve null)
	 */
	public static DBComponent getComponent(DBComponentsEnum dbComponentEnum)
	{
		return dbComponentsMap.get(dbComponentEnum);
	}
	//[end]
	
	//[start] getDBProperties
	public static DBProperties getDBProperties()
	{
		return (DBProperties)dbComponentsMap.get(DBComponentsEnum.PROPERTIES);
	}
	//[end]
}
