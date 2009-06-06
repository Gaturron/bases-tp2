package ubadb.tools.scheduleAnalyzer.ternaryLocking;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import ubadb.tools.scheduleAnalyzer.binaryLocking.BinaryLockingActionType;
import ubadb.tools.scheduleAnalyzer.common.Action;
import ubadb.tools.scheduleAnalyzer.common.Schedule;
import ubadb.tools.scheduleAnalyzer.common.ScheduleGraph;
import ubadb.tools.scheduleAnalyzer.common.ScheduleType;
import ubadb.tools.scheduleAnalyzer.common.results.LegalResult;

public class TernaryLockingSchedule extends Schedule
{
	//[start] Add Actions
	public void addAction(Action action)
	{
		actions.add((TernaryLockingAction)action);
	}
	
	public void addRLock(String transaction, String item)
	{
		actions.add(new TernaryLockingAction(TernaryLockingActionType.RLOCK, transaction, item));
	}

	public void addWLock(String transaction, String item)
	{
		actions.add(new TernaryLockingAction(TernaryLockingActionType.WLOCK, transaction, item));
	}

	public void addUnlock(String transaction, String item)
	{
		actions.add(new TernaryLockingAction(TernaryLockingActionType.UNLOCK, transaction, item));
	}

	public void addCommit(String transaction)
	{
		actions.add(new TernaryLockingAction(TernaryLockingActionType.COMMIT, transaction));
	}
	public ScheduleType getType()
	{
		return ScheduleType.TERNARY_LOCKING; 
	}
	//[end]
	
	//[start] buildScheduleGraph
	@Override
	public ScheduleGraph buildScheduleGraph()
	{
		//TODO: Completar
		//Se deben agregar arcos entre T1 -> T2 cuando:
		//- T1 hace Rlock de un ítem A y T2 luego hace WLock de A
		//- T1 hace Wlock de un ítem A y T2 luego hace RLock de A
		//- T1 hace Wlock de un ítem A y T2 luego hace WLock de A
		//OBS1: No hay arcos entre RLocks sobre el mismo ítem 
		//OBS2: No agregar arcos que se deducen por transitividad
		
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
		
		//igual que nonlocking
		
		//- Si T hace RLOCK A o WLOCK A, luego debe hacer UNLOCK A
		//- Si T hace UNLOCK A, antes debe haber hecho RLOCK A o WLOCK A
		//- Si T hace RLOCK A o WLOCK A, no puede volver a hacer RLOCK A o WLOCK A a menos que antes haya hecho UNLOCK A
		//- Si T hace RLOCK A, ninguna otra transacción T' puede hacer WLOCK A hasta que T libere a A
		//- Si T hace WLOCK A, ninguna otra transacción T' puede hacer RLOCK A o WLOCK A hasta que T libere a A
		
		Map<String, String> hashmap =new HashMap<String, String>();
		
		boolean result = true;
		
		java.util.List<Action> lst = this.getActions();
		
		for(Iterator<Action> it = lst.iterator(); it.hasNext();)
		{
			TernaryLockingAction elem = (TernaryLockingAction) it.next();
			
			if(elem.getType() == TernaryLockingActionType.RLOCK)
			{
				String key = elem.getItem();
				
				if(hashmap.containsKey(key))
				{
					if(hashmap.get(key).equals(TernaryLockingActionType.RLOCK.toString()))
					{
						//hago RL con un recurso q ya estaba con RL, todo bien
					}
					if(hashmap.get(key).equals(TernaryLockingActionType.WLOCK.toString()))
					{
						result = false;
					}
						
				}else{
					hashmap.put(key, elem.getType().toString());
				}
			}
			
			if(elem.getType() == TernaryLockingActionType.WLOCK)
			{
				String key = elem.getItem();
				
				if(hashmap.containsKey(key))
				{
					//nose puede ninguna de las dos
					if(hashmap.get(key).equals(TernaryLockingActionType.RLOCK.toString()))
					{
						result = false;
					}
					if(hashmap.get(key).equals(TernaryLockingActionType.WLOCK.toString()))
					{
						result = false;
					}
				}else{
					hashmap.put(key, elem.getType().toString());
				}
			}
			
			if(elem.getType() == TernaryLockingActionType.UNLOCK)
			{
				String key = elem.getItem();
			
				hashmap.remove(key);
			}
		}
		
		//si todas las Transacciones Unlockearon todo => el hash esta vacio
		
		result = result && hashmap.isEmpty();
		
		LegalResult legalresult = new LegalResult(
				result, 
				null, 
				null);
		
		return legalresult;
	}
	//[end]
}
