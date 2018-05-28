/*
 * Created on 27.01.2007
 * @author Stephan Richard Palm
 * Copyright by Stephan Richard Palm
 * 
 */
package net.sf.jacinth.modules;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.sf.jacinth.util.Local;

public abstract class JacinthDialog extends JDialog {

    final String title;
    final ImageIcon icon;
    final Window parent;
    
    JLabel header = new JLabel();
    JPanel areaPanel = new JPanel(new GridBagLayout());
    boolean closedWithOk = false;
    
    /**
     * All displayed information/input field come into this panel
     */
    public JPanel getAreaPanel() {
        return areaPanel;
    }
    
    public boolean isClosedWithOk() {
        return closedWithOk;
    }
    
    public JacinthDialog(Dialog owner, String title, ImageIcon icon, boolean modal) throws HeadlessException {
        super(owner, title, modal);
        this.title = title;
        this.icon = icon;
        this.parent = owner;
        init();
    }

    public JacinthDialog(Frame owner, String title, ImageIcon icon, boolean modal) throws HeadlessException {
        super(owner, title, modal);
        this.title = title;
        this.icon = icon;
        this.parent = owner;
        init();
    }

    public void replaceIcon(ImageIcon icon) {
        header.setIcon(icon);
    }
    
    protected void init() {
        setResizable(false);
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setFont(new java.awt.Font("Dialog", 0, 20));
        header.setForeground(new Color(0, 0, 124));
        header.setText(Local.getString(title));
        header.setIcon(icon);
        topPanel.setBackground(Color.WHITE);
        topPanel.add(header);

        // make border for area panel
        areaPanel.setBorder(BorderFactory.createEtchedBorder(Color.white,
                new Color(142, 142, 142)));

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton okB = new JButton();
        JButton cancelB = new JButton();
        okB.setMaximumSize(new Dimension(100, 26));
        okB.setMinimumSize(new Dimension(100, 26));
        okB.setPreferredSize(new Dimension(100, 26));
        okB.setText("Ok");
        okB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                okB_actionPerformed(e);
            }
        });
        
        // Set the ok button as default button
        this.getRootPane().setDefaultButton(okB);

        cancelB.setMaximumSize(new Dimension(100, 26));
        cancelB.setMinimumSize(new Dimension(100, 26));
        cancelB.setPreferredSize(new Dimension(100, 26));
        cancelB.setText(Local.getString("Cancel"));
        cancelB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cancelB_actionPerformed(e);
            }
        });
        buttonsPanel.add(okB);
        buttonsPanel.add(cancelB);

        this.getContentPane().add(topPanel, BorderLayout.NORTH);
        this.getContentPane().add(areaPanel, BorderLayout.CENTER);
        this.getContentPane().add(buttonsPanel, BorderLayout.SOUTH);
    }

    protected void okB_actionPerformed(ActionEvent e) {
        closedWithOk = true;
        this.dispose();
    }
    
    protected void cancelB_actionPerformed(ActionEvent e) {
        this.dispose();
    }
    
    public void positionDialog() {
        Dimension dlgSize = getPreferredSize();
        if (parent != null) {
            Dimension frmSize = parent.getSize();
            Point loc = parent.getLocationOnScreen();
            setLocation((frmSize.width - dlgSize.width) / 2 + loc.x,
                    (frmSize.height - dlgSize.height) / 2 + loc.y);
        } else {
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            setLocation((screenSize.width - dlgSize.width) / 2,
                    (screenSize.height - dlgSize.height) / 2);
        }
    }
    
}
