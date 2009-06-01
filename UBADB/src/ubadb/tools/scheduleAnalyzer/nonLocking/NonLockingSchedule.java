package ubadb.tools.scheduleAnalyzer.nonLocking;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

import ubadb.tools.scheduleAnalyzer.common.Action;
import ubadb.tools.scheduleAnalyzer.common.Schedule;
import ubadb.tools.scheduleAnalyzer.common.ScheduleGraph;
import ubadb.tools.scheduleAnalyzer.common.ScheduleType;
import ubadb.tools.scheduleAnalyzer.common.results.LegalResult;

public class NonLockingSchedule extends Schedule
{
	//[start] Add Actions
	public void addAction(Action action)
	{
		actions.add((NonLockingAction)action);
	}
	
	public void addRead(String transaction, String item)
	{
		actions.add(new NonLockingAction(NonLockingActionType.READ, transaction, item));
	}

	public void addWrite(String transaction, String item)
	{
		actions.add(new NonLockingAction(NonLockingActionType.WRITE, transaction, item));
	}

	public void addCommit(String transaction)
	{
		actions.add(new NonLockingAction(NonLockingActionType.COMMIT, transaction));
	}
	public ScheduleType getType()
	{
		return ScheduleType.NON_LOCKING; 
	}
	//[end]
	
	//[start] buildScheduleGraph
	@Override
	public ScheduleGraph buildScheduleGraph()
	{
		//TODO: Completar
		//Se deben agregar arcos entre T1 -> T2 cuando:
		//- T1 lee un ítem A y T2 luego escribe A
		//- T1 escribe un ítem A y T2 luego lee A
		//- T1 escribe un ítem A y T2 luego escribe A
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
		
		//Esto lo hago para entender como acceder a una transaccion
		//cada Transaccion tiene una lista de acciones, 
		//cada accion tiene booleano qe dice si hace R, W o C
		//lo siguiente es para imprimir lo que hace
		//System.out.println("Ejecuto analizar legalidad:");
		
		//System.out.println(this.getTransactions().toString());
		//System.out.println(this.getItems().toString());
		//System.out.println(this.getActions().toString());
		
		//java.util.List<Action> lst = this.getActions();
		//System.out.println("Acciones tiene:");
		//for(Iterator<Action> it = lst.iterator(); it.hasNext();)
		//{
		//	Action elem = it.next();
		//	System.out.println(" R:"+elem.reads()+" W:"+elem.writes()+" C:"+elem.commits()+" "+elem.getItem()+" "+elem.getTransaction());
		//}
		
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
		
		//si la transaccon no es legal, hay q devolver alguna no legal
		String illegalTransaction = null;
		String message = "OK";
		if(! transacciones.isEmpty())
		{
			illegalTransaction = transacciones.get(0);
			message = "Falló "+illegalTransaction;
		}
		
		LegalResult result = new LegalResult(
				transacciones.isEmpty(), 
				illegalTransaction, 
				message);
		
		System.out.println(result.toString());
		return result;
	}
	//[end]
}
