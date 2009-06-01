package ubadb.tools.scheduleAnalyzer.gui.forms;

import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import ubadb.tools.scheduleAnalyzer.common.Action;
import ubadb.tools.scheduleAnalyzer.common.Schedule;
import ubadb.tools.scheduleAnalyzer.common.results.LegalResult;
import ubadb.tools.scheduleAnalyzer.common.results.RecoverabilityResult;
import ubadb.tools.scheduleAnalyzer.common.results.RecoverabilityType;
import ubadb.tools.scheduleAnalyzer.common.results.SerialResult;
import ubadb.tools.scheduleAnalyzer.common.results.SerializabilityResult;

@SuppressWarnings("serial")
public class AnalyzeScheduleDialog extends JDialog
{
    /** Creates new form ScheduleAnalyzerForm 
     * @param schedule 
     * @param b 
     * @param parent */
    public AnalyzeScheduleDialog(Frame parent, boolean modal, Schedule schedule)
    {
    	super(parent, modal);
        initComponents();
        
        // Analizo cada aspecto de la historia
        AnalyzeLegality(schedule);
        AnalyzeSeriality(schedule);
        AnalyzeSerializability(schedule);
        AnalyzeRecoverability(schedule);
    }
    
    /**
     * Analiza y muestra los resultados de la legalidad de la historia
     * @param schedule
     */
	private void AnalyzeLegality(Schedule schedule)
	{
    	// Lanzo el analizador
		LegalResult result = schedule.analyzeLegality();
		
		// Relleno el dialogo con el resultado
		if(result.isLegal())
		{
			lblResumenLegal.setText("Si");
			lblLegalTransaction.setText("La historia es legal");
		}
		else	
		{
			lblResumenLegal.setText("No");
			lblLegalTransaction.setText("La historia es ilegal, una transaccion ilegal es " + result.getIllegalTransaction());
		}
		
		lblLegalMessage.setText(result.getMessage());
	}

    /**
     * Analiza y muestra los resultados de la serialidad de la historia
     * @param schedule
     */
    private void AnalyzeSeriality(Schedule schedule)
	{
    	// Lanzo el analizador
		SerialResult result = schedule.analyzeSeriality();
		
		// Relleno el dialogo con el resultado
		if(result.isSerial())
		{
			lblResumenSerial.setText("Si");
			lblSerialTransaction.setText("La historia es serial");
		}
		else
		{
			lblResumenSerial.setText("No");
			lblSerialTransaction.setText("La historia no es serial, una transaccion no serial es " + result.getNonSerialTransaction());
		}
		
		lblSerialMessage.setText(result.getMessage());
	}

    /**
     * Analiza y muestra los resultados de la seriabilizidad de la historia
     * @param schedule
     */
	private void AnalyzeSerializability(Schedule schedule)
	{
		// Lanzo el analizador
		SerializabilityResult result = schedule.analyzeSerializability();
		
		// Relleno el dialogo con el resultado
		if(result.isSerializable())
		{
			lblResumenSerializable.setText("Si");
			
			// Serializo la lista de ejecuciones
			String executions = "La historia es serializable. Las posibles ejecuciones son: \n";
			for(int i = 0; i < result.getPossibleExecutions().size(); ++i)
			{
				for(int j = 0; j < result.getPossibleExecutions().get(i).size(); ++j)
				{
					Action currentAction = result.getPossibleExecutions().get(i).get(j);
					executions += currentAction.toString(true) + " ";
				}
				
				executions += "\n";
			}
			
			lblSerializableExecutions.setText(executions);
		}
		else
		{
			lblResumenSerializable.setText("No");
			
			// Serializo el ciclo
			String cycle = "La historia no es serializable, se forma un ciclo entre las transacciones \n";
			for(int i = 0; i < result.getCycle().size(); ++i)
			{
				cycle += result.getCycle().get(i) + " ";
			}
			
			lblSerializableExecutions.setText(cycle);
		}
		
		lblSerializableMessage.setText(result.getMessage());		
	}

    /**
     * Analiza y muestra los resultados de la recuperabilidad de la historia
     * @param schedule
     */
	private void AnalyzeRecoverability(Schedule schedule)
	{
		// Lanzo el analizador
		RecoverabilityResult result = schedule.analyzeRecoverability();
		
		// Relleno el dialogo con el resultado
		String tipo = "La recuperabilidad de la historia es: ";
		if(result.getType() == RecoverabilityType.NON_RECOVERABLE)
		{
			tipo += "No recuperable";
		}
		else if(result.getType() == RecoverabilityType.RECOVERABLE)
		{
			tipo += "Recuperable";
		}
		else if(result.getType() == RecoverabilityType.AVOIDS_CASCADING_ABORTS)
		{
			tipo += "Evita aborts en cascada";
		}
		else if(result.getType() == RecoverabilityType.STRICT)
		{
			tipo += "Estricta";
		} 
		
		lblRecuperabilidadTipo.setText(tipo);
		
		// Asumo que si es de tipo estricto no hay txs en conflicto, por lo cual no las muestro
		if(result.getType() == RecoverabilityType.STRICT)
		{
			lblRecuperabilidadTxs.setText("Por ser estricta, la historia no presenta transacciones en conflicto");
		}
		else
		{
			lblRecuperabilidadTxs.setText("Las transacciones en conflicto son " + 
					result.getTransaction1() + " y " + result.getTransaction2());
		}
		
		lblSerialMessage.setText(result.getMessage());
	}

	
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        lblResumenRecuperabilidad = new javax.swing.JLabel();
        lblResumenLegal = new javax.swing.JLabel();
        lblResumenSerial = new javax.swing.JLabel();
        lblResumenSerializable = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        lblLegalTransaction = new javax.swing.JLabel();
        lblLegalMessage = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        lblSerialTransaction = new javax.swing.JLabel();
        lblSerialMessage = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        lblSerializableExecutions = new javax.swing.JLabel();
        lblSerializableMessage = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        lblRecuperabilidadTipo = new javax.swing.JLabel();
        lblRecuperabilidadTxs = new javax.swing.JLabel();
        lblRecuperabilidadMensaje = new javax.swing.JLabel();
        butCerrar = new javax.swing.JButton();

        setTitle("Analisis de historia");

        jLabel1.setText("¿Es legal?");

        jLabel4.setText("¿Es serial?");

        jLabel6.setText("¿Es serializable?");

        jLabel8.setText("Recuperabilidad");

        lblResumenRecuperabilidad.setText("-");

        lblResumenLegal.setText("-");

        lblResumenSerial.setText("-");

        lblResumenSerializable.setText("-");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblResumenLegal))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblResumenSerial))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblResumenSerializable))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(109, 109, 109)
                        .addComponent(lblResumenRecuperabilidad)))
                .addContainerGap(296, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(lblResumenLegal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(lblResumenSerial))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(lblResumenSerializable))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(lblResumenRecuperabilidad))
                .addContainerGap(51, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Resumen", jPanel1);

        lblLegalTransaction.setText("Es legal | Tx ilegal");

        lblLegalMessage.setText("Mensaje");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblLegalTransaction)
                    .addComponent(lblLegalMessage))
                .addContainerGap(412, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblLegalTransaction)
                .addGap(18, 18, 18)
                .addComponent(lblLegalMessage)
                .addContainerGap(104, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("¿Es legal?", jPanel2);

        lblSerialTransaction.setText("Es serial | Tx no serial");

        lblSerialMessage.setText("Mensaje");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblSerialTransaction)
                    .addComponent(lblSerialMessage))
                .addContainerGap(393, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblSerialTransaction)
                .addGap(18, 18, 18)
                .addComponent(lblSerialMessage)
                .addContainerGap(104, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("¿Es serial?", jPanel3);

        lblSerializableExecutions.setText("Ejecuciones | Ciclo");

        lblSerializableMessage.setText("Mensaje");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblSerializableExecutions)
                    .addComponent(lblSerializableMessage))
                .addContainerGap(409, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblSerializableExecutions)
                .addGap(18, 18, 18)
                .addComponent(lblSerializableMessage)
                .addContainerGap(104, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("¿Es serializable?", jPanel4);

        lblRecuperabilidadTipo.setText("Tipo");

        lblRecuperabilidadTxs.setText("Txs en conflicto");

        lblRecuperabilidadMensaje.setText("Mensaje");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblRecuperabilidadTipo)
                    .addComponent(lblRecuperabilidadTxs)
                    .addComponent(lblRecuperabilidadMensaje))
                .addContainerGap(422, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblRecuperabilidadTipo)
                .addGap(18, 18, 18)
                .addComponent(lblRecuperabilidadTxs)
                .addGap(18, 18, 18)
                .addComponent(lblRecuperabilidadMensaje)
                .addContainerGap(72, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Recuperabilidad", jPanel5);

        butCerrar.setText("Cerrar");
        butCerrar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                butCerrarMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(459, Short.MAX_VALUE)
                .addComponent(butCerrar)
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 512, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(211, Short.MAX_VALUE)
                .addComponent(butCerrar)
                .addContainerGap())
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(45, Short.MAX_VALUE)))
        );

        pack();
    }//GEN-END:initComponents

    private void butCerrarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_butCerrarMouseClicked
        this.setVisible(false);
    }//GEN-LAST:event_butCerrarMouseClicked

    public static void showDialog(Frame parent, Schedule schedule)
    {
    	AnalyzeScheduleDialog dialog = new AnalyzeScheduleDialog(parent, true, schedule);
        dialog.setVisible(true);
    }
    
	/**
     * Muestra un mensaje de error al usuario
     * @param message
     */
	public void ShowErrorMessage(String message)
	{
		JOptionPane.showConfirmDialog(
			this,
			message,
			"Error",
			JOptionPane.DEFAULT_OPTION,
			JOptionPane.ERROR_MESSAGE);
	}
	
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton butCerrar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblLegalMessage;
    private javax.swing.JLabel lblLegalTransaction;
    private javax.swing.JLabel lblRecuperabilidadMensaje;
    private javax.swing.JLabel lblRecuperabilidadTipo;
    private javax.swing.JLabel lblRecuperabilidadTxs;
    private javax.swing.JLabel lblResumenLegal;
    private javax.swing.JLabel lblResumenRecuperabilidad;
    private javax.swing.JLabel lblResumenSerial;
    private javax.swing.JLabel lblResumenSerializable;
    private javax.swing.JLabel lblSerialMessage;
    private javax.swing.JLabel lblSerialTransaction;
    private javax.swing.JLabel lblSerializableExecutions;
    private javax.swing.JLabel lblSerializableMessage;
    // End of variables declaration//GEN-END:variables
}
