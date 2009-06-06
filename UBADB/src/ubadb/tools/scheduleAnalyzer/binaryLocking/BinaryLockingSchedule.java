package ubadb.tools.scheduleAnalyzer.binaryLocking;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ubadb.tools.scheduleAnalyzer.common.Action;
import ubadb.tools.scheduleAnalyzer.common.Schedule;
import ubadb.tools.scheduleAnalyzer.common.ScheduleGraph;
import ubadb.tools.scheduleAnalyzer.common.ScheduleType;
import ubadb.tools.scheduleAnalyzer.common.results.LegalResult;
import ubadb.tools.scheduleAnalyzer.nonLocking.NonLockingSchedule;

public class BinaryLockingSchedule extends Schedule
{
	//[start] Add Actions
	public void addAction(Action action)
	{
		actions.add((BinaryLockingAction)action);
	}
	
	public void addLock(String transaction, String item)
	{
		actions.add(new BinaryLockingAction(BinaryLockingActionType.LOCK, transaction, item));
	}

	public void addUnlock(String transaction, String item)
	{
		actions.add(new BinaryLockingAction(BinaryLockingActionType.UNLOCK, transaction, item));
	}

	public void addCommit(String transaction)
	{
		actions.add(new BinaryLockingAction(BinaryLockingActionType.COMMIT, transaction));
	}
	public ScheduleType getType()
	{
		return ScheduleType.BINARY_LOCKING; 
	}
	//[end]
	
	//[start] buildScheduleGraph
	@Override
	public ScheduleGraph buildScheduleGraph()
	{
		//TODO: Completar
		//Se deben agregar arcos entre T1 -> T2 cuando:
		//- T1 hace lock de un ítem A y T2 hace lock de A
		//OBS: No agregar arcos que se deducen por transitividad
		
		return null;
	}
	//[end]

	//[start] analyzeLegality
	@Override
	public LegalResult analyzeLegality()
	{
		//TODO: Completar
		//Un schedule es legal cuando:
		//- Cada transacción T posee como máximo un commit
		
		//agarro las transacciones
		java.util.List<String> transacciones = this.getTransactions();
		
		//agarro las acciones de la historia
		java.util.List<Action> lst = this.getActions();
		for(Iterator<Action> it = lst.iterator(); it.hasNext();)
		{
			Action elem = it.next();
			if(elem.commits())
			{
				//si tiene commit, tengo que ver que sea la ultima operacion
				//de esa transaccion
				boolean ultima = true;
				for(Iterator<Action> it1 = it; it.hasNext();)
				{
					Action elem1 = it1.next();
					
					if(elem1.getTransaction() == elem.getTransaction())
					{
						//todas las prox acciones tienen que ser de otras transacciones
						// o sea distintas de elem
						ultima = false;
					}
				}
					
				//si elem es commit Y era la ultima transaccion => lo saco de la lista transacciones
				if(ultima) transacciones.remove(elem.getTransaction());
			}
		}
		
		//si transacciones es vacia quiere decir que todas tuvieron a lo sumo 1 commit
		
		boolean resCommitOK = false;
		if(transacciones.isEmpty()) resCommitOK = true;
		
		// igual que nonlocking 
		
		//- Si T hace LOCK A, luego debe hacer UNLOCK A
		//- Si T hace UNLOCK A, antes debe haber hecho LOCK A
		//- Si T hace LOCK A, ninguna otra transacción T' puede hacer LOCK A hasta que T libere a A
		
		Set <String> usados = new HashSet <String>();
				
		boolean resLOCK = true;
		
		lst = this.getActions();
		
		for(Iterator<Action> it = lst.iterator(); it.hasNext();)
		{
			BinaryLockingAction elem = (BinaryLockingAction)it.next();
		
			if(elem.getType() == BinaryLockingActionType.LOCK)
			{
				String key = elem.getItem();
				
				//- Si T hace LOCK A, no puede volver a hacer LOCK A a menos que antes haya hecho UNLOCK A
				if(usados.contains(key))
				{
					resLOCK = false;
				}else{
					usados.add(key);
				}
			}
			
			if(elem.getType() == BinaryLockingActionType.UNLOCK)
			{
				String key = elem.getItem();
			
				if(usados.contains(key))
				{
					usados.remove(key);
				}else{
					resLOCK = false;
				}
			}
		}
		
		//si todas las Transacciones Unlockearon todo => el hash esta vacio
		
		resLOCK = resLOCK && usados.isEmpty();
		
		LegalResult result = new LegalResult(
				resCommitOK && resLOCK, 
				null, 
				null);
		
		return result;
	}
	//[end]
}
