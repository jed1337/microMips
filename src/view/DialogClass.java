/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Nonbr
 */
class DialogClass {
    MainFrame mf;
    JDialog dialog;
    String memoryAddress;
  
    public DialogClass(MainFrame mf) {
            this.mf = mf;
        }
    
        public void setMemoryAddress(String memoryAddress) {
            this.memoryAddress = memoryAddress;
        }

        public void createDialog(String memoryAddress, int toggle, int row) {
            dialog = new JDialog(mf, "Enter new value...", false);
            final JTextField textField = new JTextField(16);
            JPanel valuePanel = new JPanel();
            valuePanel.add(textField);
            JButton send = new JButton("Done");
            send.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String text = textField.getText();
                    mf.setValue(memoryAddress, text, toggle, row);
                    dialog.dispose();
                }
            });
            valuePanel.add(send);
            dialog.getContentPane().add(valuePanel, 0);
            dialog.setSize(200,100);
            dialog.setLocationRelativeTo(mf);
        }

        public void showDialog()
        {
            dialog.setVisible(true);
        }
    }
