package ubadb.tools.scheduleAnalyzer.gui.forms.tools;

import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class ScheduleTableModel extends DefaultTableModel
{
	public ScheduleTableModel()
	{  
	   super();  
	}
	
	public boolean isCellEditable(int row, int col)
	{  
	   return false;  
	}  
}
