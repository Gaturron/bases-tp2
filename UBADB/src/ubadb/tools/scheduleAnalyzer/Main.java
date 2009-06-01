package ubadb.tools.scheduleAnalyzer;

import ubadb.tools.scheduleAnalyzer.gui.forms.ScheduleAnalyzerForm;

public class Main
{
    //[start] Main
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ScheduleAnalyzerForm().setVisible(true);
            }
        });
    }
	//[end]
}
