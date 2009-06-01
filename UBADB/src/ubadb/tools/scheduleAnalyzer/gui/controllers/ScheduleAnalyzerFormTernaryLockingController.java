package ubadb.tools.scheduleAnalyzer.gui.controllers;

import ubadb.tools.scheduleAnalyzer.common.Action;
import ubadb.tools.scheduleAnalyzer.common.Schedule;
import ubadb.tools.scheduleAnalyzer.exceptions.ScheduleException;
import ubadb.tools.scheduleAnalyzer.gui.forms.EditTernaryLockingActionDialog;
import ubadb.tools.scheduleAnalyzer.gui.forms.ScheduleAnalyzerForm;
import ubadb.tools.scheduleAnalyzer.gui.forms.tools.ScheduleTableModel;
import ubadb.tools.scheduleAnalyzer.ternaryLocking.TernaryLockingSchedule;

public class ScheduleAnalyzerFormTernaryLockingController extends ScheduleAnalyzerFormController
{
	TernaryLockingSchedule schedule = null;
		
	public ScheduleAnalyzerFormTernaryLockingController(ScheduleAnalyzerForm form)
	{
		// Guardo e inicializo las variables del control 
		this.form = form;
		this.formTable = form.getTableSchedule(); 
		this.formTableModel =  ((ScheduleTableModel)formTable.getModel());
		this.schedule = new TernaryLockingSchedule(); 
	}
	
	@Override
	protected Schedule getSchedule()
	{
		return schedule;
	}
	
	@Override
	protected void setSchedule(Schedule schedule)
	{
		this.schedule = (TernaryLockingSchedule)schedule; 
	}
	
	@Override
	protected Action ShowEditActionDialog()
	{
    	// Invoco a la modificacion del item que voy a agregar
    	return EditTernaryLockingActionDialog.showDialog(
    			form, 
    			schedule.getTransactions(),
    			schedule.getItems());
	}

	@Override
	public String InnerAddNewItem() throws ScheduleException
	{
		// Muestro el dialogo para que el usuario ingrese el nombre del item
		String item = form.ShowInputDialog("Nombre del nuevo item:");
		
		// Si el usuario no cancelo el dialogo, agrego el item
		if(item != null)
		{
			schedule.addItem(item);
		}
		
		return item;
	}
}
