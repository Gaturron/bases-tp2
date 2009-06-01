package ubadb.tools.scheduleAnalyzer.gui.controllers;

import ubadb.tools.scheduleAnalyzer.binaryLocking.BinaryLockingSchedule;
import ubadb.tools.scheduleAnalyzer.common.Action;
import ubadb.tools.scheduleAnalyzer.common.Schedule;
import ubadb.tools.scheduleAnalyzer.exceptions.ScheduleException;
import ubadb.tools.scheduleAnalyzer.gui.forms.EditBinaryLockingActionDialog;
import ubadb.tools.scheduleAnalyzer.gui.forms.ScheduleAnalyzerForm;
import ubadb.tools.scheduleAnalyzer.gui.forms.tools.ScheduleTableModel;

public class ScheduleAnalyzerFormBinaryLockingController extends ScheduleAnalyzerFormController
{
	BinaryLockingSchedule schedule = null;
		
	public ScheduleAnalyzerFormBinaryLockingController(ScheduleAnalyzerForm form)
	{
		// Guardo e inicializo las variables del control 
		this.form = form;
		this.formTable = form.getTableSchedule(); 
		this.formTableModel =  ((ScheduleTableModel)formTable.getModel());
		this.schedule = new BinaryLockingSchedule(); 
	}
	
	@Override
	protected Schedule getSchedule()
	{
		return schedule;
	}
	
	@Override
	protected void setSchedule(Schedule schedule)
	{
		this.schedule = (BinaryLockingSchedule)schedule; 
	}
	
	@Override
	protected Action ShowEditActionDialog()
	{
    	// Invoco a la modificacion del item que voy a agregar
    	return EditBinaryLockingActionDialog.showDialog(
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
