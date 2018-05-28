package net.sf.jacinth;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class SrcDialog extends JDialog {
    
    final static String SOURCE = "Source text";
    
	JPanel panel1 = new JPanel();
	BorderLayout borderLayout1 = new BorderLayout();
	JScrollPane jScrollPane1 = new JScrollPane();
	JTextArea jTextArea1 = new JTextArea();

      /**
         * This function allows to get a new source dialog by giving the
         * component that wants to show it. The function searches the parent
         * Dialog or Frame from the invoking component. This way no headless
         * problems occur.
         * 
         * @param component
         * @return
         */
    static public SrcDialog getNewSourceDialog(Component component, String text) {
        while (component != null && !(component instanceof Frame)
                && !(component instanceof Dialog))
            component = component.getParent();

        if (component == null)
            return new SrcDialog((Frame) null, text);
        else if (component instanceof Dialog)
            return new SrcDialog((Dialog) component, text);
        else
            return new SrcDialog((Frame) component, text);
    }

    private SrcDialog(Frame frame, String text) {
        super(frame, SOURCE, true);
        try {
            setText(text);
            jbInit();
            pack();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private SrcDialog(Dialog dialog, String text) {
        super(dialog, SOURCE, true);
        try {
            setText(text);
            jbInit();
            pack();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

	
	void jbInit() throws Exception {
		panel1.setLayout(borderLayout1);
		jTextArea1.setEditable(false);
		getContentPane().add(panel1);
		panel1.add(jScrollPane1, BorderLayout.CENTER);
		jScrollPane1.getViewport().add(jTextArea1, null);
	}

	public void setText(String txt) {
		jTextArea1.setText(txt);
	}
}