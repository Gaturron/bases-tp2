package ubadb.tools.scheduleAnalyzer.common;

import java.util.ArrayList;
import java.util.List;

public class ScheduleGraph
{
	//[start] Atributos
	private List<ScheduleArc> arcs;
	private List<String> transactions;
	//[end]
	
	//[start] Constructor
	public ScheduleGraph()
	{
		arcs = new ArrayList<ScheduleArc>();
		transactions = new ArrayList<String>();
	}
	//[end]
	
	//[start] addTransaction
	public void addTransaction(String transaction)
	{
		transactions.add(transaction);
	}
	//[end]
	
	//[start] getTransactions
	public List<String> getTransactions()
	{
		return transactions;
	}
	//[end]
	
	//[start] addArc
	public void addArc(ScheduleArc arc)
	{
		arcs.add(arc);
	}
	//[end]
	
	//[start] getArcs
	public List<ScheduleArc> getArcs()
	{
		return arcs;
	}
	//[end]
}
