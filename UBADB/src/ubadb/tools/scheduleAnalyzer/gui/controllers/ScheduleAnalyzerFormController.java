package ubadb.tools.scheduleAnalyzer.gui.controllers;


import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;

import ubadb.tools.scheduleAnalyzer.common.Action;
import ubadb.tools.scheduleAnalyzer.common.Schedule;
import ubadb.tools.scheduleAnalyzer.common.ScheduleType;
import ubadb.tools.scheduleAnalyzer.exceptions.ScheduleException;
import ubadb.tools.scheduleAnalyzer.gui.forms.AnalyzeScheduleDialog;
import ubadb.tools.scheduleAnalyzer.gui.forms.ScheduleAnalyzerForm;
import ubadb.tools.scheduleAnalyzer.gui.forms.tools.ScheduleTableModel;

public abstract class ScheduleAnalyzerFormController
{
	protected ScheduleAnalyzerForm form = null;
	protected JTable formTable = null;
	protected ScheduleTableModel formTableModel = null;
	
	/**
	 * Devuelve una instancia especifica de controlador que corresponda al tipo de historia dada
	 * @param form
	 * @param scheduleType
	 * @return
	 */
	public static ScheduleAnalyzerFormController CreateController(ScheduleAnalyzerForm form, ScheduleType scheduleType)
	{
		// Creo un controlador en funcion del tipo de la historia que me dieron y lo inicializo
		ScheduleAnalyzerFormController ret = SelectControllerByScheduleType(form, scheduleType);
		ret.InitializeForm();
		
		return ret;
	}

	
	/**
	 * Devuelve una instancia especifica de controlador que corresponda al tipo de la la historia dada y la 
	 * carga como la historia actual
	 * @param form
	 * @param schedule
	 * @return
	 */
	public static ScheduleAnalyzerFormController CreateController(ScheduleAnalyzerForm form, Schedule schedule)
	{
		// Creo un controlador en funcion del tipo de la historia que me dieron 
		ScheduleAnalyzerFormController ret = SelectControllerByScheduleType(form, schedule.getType());
		
		// Al nuevo controlador le cargo la historia
		ret.LoadSchedule(schedule);
		
		return ret;
	}
	
	/**
	 * Devuelve el controlador acorde al tipo de historia dada
	 * @param form
	 * @param scheduleType
	 * @return
	 */
	private static ScheduleAnalyzerFormController SelectControllerByScheduleType(
			ScheduleAnalyzerForm form, ScheduleType scheduleType)
	{
		ScheduleAnalyzerFormController ret = null;
		
		if(scheduleType == ScheduleType.NON_LOCKING)
		{
			ret = new ScheduleAnalyzerFormNonLockingController(form);
		}
		else if(scheduleType == ScheduleType.BINARY_LOCKING)
		{
			ret = new ScheduleAnalyzerFormBinaryLockingController(form);
		}
		else if(scheduleType == ScheduleType.TERNARY_LOCKING)
		{
			ret = new ScheduleAnalyzerFormTernaryLockingController(form);
		}

		return ret;
	}
	
	/**
	 * Inicializa el formulario
	 */
	private void InitializeForm()
	{
		// Limpio la tabla
		ClearScheduleOnForm();
		
        // Agrego la columna de numero de item
       formTableModel.addColumn("#Paso");
       
       // Habilito los controles
       form.EnableUserControls();
	}

	/**
	 * Borra toda informacion que halla de la historia actual en el formulario
	 */
	private void ClearScheduleOnForm()
	{
		// Borro todas las filas de la tabla
		formTableModel.getDataVector().removeAllElements();
	
		// Borro todas las columnas de la tabla
		TableColumnModel columnModel = formTable.getColumnModel();
		while(formTable.getColumnCount() > 0)
		{
			formTable.removeColumn(columnModel.getColumn(0));
		}
		//Además de remover las columnas, hay que llamar a este método (aparentemente, bug de Java: http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4283734)
		formTableModel.setDataVector( new Vector<Object>(), new Vector<Object>());
		
		// Borro la lista de items
		DefaultListModel listModel = (DefaultListModel)form.getItemsList().getModel();
		listModel.clear();
	}
	
	/**
	 * Le pregunta al usuario un nombre para una nueva transaccion
	 * @return
	 */
	protected String SelectNewTxName()
	{
		return form.ShowInputDialog("Nombre de la nueva transaccion:");
	}

	/**
	 * Le muestra al usuario el reporte del analisis de la historia
	 */
	public void AnalyzeSchedule()
	{
		// Lanzo el dialogo en donde se analizara la historia
		AnalyzeScheduleDialog.showDialog(form, getSchedule());
	}
	
	/**
	 * Agrega una nueva transaccion (columna) a la tabla
	 * @throws ScheduleException 
	 */
	public void AddNewTransaction() throws ScheduleException
	{
		String newTxName = SelectNewTxName();
		
		// Si el usuario no cancelo el dialogo, agrego la nueva transaccion
		if(newTxName != null)
		{
			getSchedule().addTransaction(newTxName);
			AddTransactionToTable(newTxName);
		}
	}
	
	/**
	 * Agrega un nuevo paso (fila) a la tabla
	 * @throws ScheduleException
	 */
	public void AddNewStep() throws ScheduleException
	{
		// Si no hay items o transacciones, no puedo agregar una nueva accion
		if(getSchedule().getTransactions().isEmpty())
		{
			throw new ScheduleException("Debe agregarse al menos una transaccion antes de agregar un paso");
		}
		else if(getSchedule().getItems().isEmpty())
		{
			throw new ScheduleException("Debe agregarse al menos un item antes de agregar un paso");
		}
		else
		{
			// Lanzo el dialogo para la edicion de la accion
			Action action = ShowEditActionDialog();
			
			// Si no se eligio una accion no sigo
	    	if(action == null)
	    	{
	    		return;
	    	}
	    	
	    	// Agrego la accion a la historia
	    	getSchedule().addAction(action);
	    	
	    	// Agrego el paso a la tabla
	    	AddStepToTable(action);
		}
	}
	
	/**
     * Agrega un nuevo item a la historia
     */
	public void AddNewItem() throws ScheduleException
	{
		// Invoco al metodo concreto para agregar al nuevo item
		String newItem = InnerAddNewItem();
		
		// Si se agrego un item, lo agrego a la lista de items en el formulario
		if(newItem != null)
		{
			// Agrego el item a la lista
			AddItemToList(newItem);
		}
	}

	/**
	 * Elimina la historia actual y carga la dada
	 * @param schedule
	 */
	private void LoadSchedule(Schedule schedule)
	{
		// Piso la historia actual
		this.setSchedule(schedule);
		
		// Inicializo el formulario
		this.InitializeForm();
		
		// Muestro la historia en la tabla
		ShowSchedule(schedule);
	}
	
	/**
	 * Devuelve la historia formada actualmente
	 * @return
	 */
	public Schedule GetSchedule()
	{
		return getSchedule();
	}
	
	/**
	 * Muestra la historia dada en el formulario, pisando la anterior
	 * @param schedule
	 */
	private void ShowSchedule(Schedule schedule)
	{
		// Agrego los items
		for(int i = 0; i < schedule.getItems().size(); ++i)
		{
			this.AddItemToList(schedule.getItems().get(i));
		}
		
		// Agrego las transacciones
		for(int i = 0; i < schedule.getTransactions().size(); ++i)
		{
			this.AddTransactionToTable(schedule.getTransactions().get(i));
		}
		
		// Agrego los pasos
		for(int i = 0; i < schedule.getActions().size(); ++i)
		{
			this.AddStepToTable(schedule.getActions().get(i));
		}
	}
	
    /**
     * Muestra el dialogo correspondiende a la edicion del item con el numero dado.
     */
    public void EditAction(int actionNumber) throws ScheduleException
    {
    	// Lanzo el editor de pasos concreto
    	Action action = ShowEditActionDialog();
    	
		// Si no hay accion es porque se cancelo el dialogo
		if(action == null)
		{
			return;
		}
		
		// Agrego la accion a la historia
		getSchedule().editAction(actionNumber, action);
		
		// Limpio las demas celdas donde podria haber otros valores
		for(int column = 1; column < formTableModel.getColumnCount(); ++column)
		{
			formTableModel.setValueAt("", actionNumber, column);
		}
		
		// Modifico la tabla con el nuevo valor 
		formTableModel.setValueAt(action.toString(false), 
			actionNumber,
			formTableModel.findColumn(action.getTransaction()));
	}
	
	/**
	 * Agrega un paso a la tabla en el formulario
	 * @param newItemNumer
	 * @param newAction
	 */
	protected void AddStepToTable(Action action)
	{
		// Averiguo el numero del nuevo item
    	int itemNumer = formTableModel.getRowCount() + 1;

		// Agrego la fila a la tabla con el numero de paso en el primer campo
		int transactionColumnIndex = formTableModel.findColumn(action.getTransaction());
    	Object[] newRow = new Object[formTableModel.getColumnCount() + 1];
    	newRow[0] =  Integer.toString(itemNumer);
    	newRow[transactionColumnIndex] = action.toString(false);
    	formTableModel.addRow(newRow);
	}
	
	/**
	 * Agrega un item a la lista de items en el formulario
	 * @param newItem
	 */
	private void AddItemToList(String newItem)
	{
		DefaultListModel listModel = (DefaultListModel)form.getItemsList().getModel();
		listModel.add(listModel.getSize(), newItem);
	}
	
	/**
	 * Agrega una transaccion a la tabla en el formulario
	 * @param newTxName
	 */
	private void AddTransactionToTable(String newTxName)
	{
		formTableModel.addColumn(newTxName);
	}
	
	
    /**
     * Logica concreta de cada implementacion a la hora de agregar un nuevo item
     */
	protected abstract String InnerAddNewItem() throws ScheduleException;
	
	/**
	 * Muestra el dialogo de edicion de accion
	 * @return
	 * @throws ScheduleException
	 */
	protected abstract Action ShowEditActionDialog() throws ScheduleException;
	
	/**
	 * Devuelve la historia que mantiene cada implementacion por su cuenta
	 * @return
	 */
	protected abstract Schedule getSchedule();
	
	/**
	 * Pisa la historia que mantiene cada implementacion con la dada
	 * @param schedule
	 */
	protected abstract void setSchedule(Schedule schedule);
}
