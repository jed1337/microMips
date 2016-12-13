/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import utilities.Errors;

/**
 *
 * @author Nonbr
 */
class DialogClass {

    private static final Pattern P_VALUE = Pattern.compile("([0-9a-fA-F]{1,16})");
    private MainFrame mf;
    private JDialog dialog;
    private JDialog errorDialog;
    private String memoryAddress;

    DialogClass(MainFrame mf) {
        this.mf = mf;
    }

    public void setMemoryAddress(String memoryAddress) {
        this.memoryAddress = memoryAddress;
    }

    public void createDialog(String memoryAddress, int toggle, int row, int purpose) {
        if (purpose == 0) {
            dialog = new JDialog(mf, "Enter new value...", false);
        } else if (purpose == 1) {
            dialog = new JDialog(mf, "Enter a memory location...", false);
        }

        final JTextField textField = new JTextField(20);
        JPanel valuePanel = new JPanel();
        valuePanel.add(textField);
        JButton send = new JButton("Done");
        send.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String text = textField.getText();

                Matcher matcher = P_VALUE.matcher(text);
                if (!matcher.find() && text.length() > 16) {
                    text = null;
                    Errors.addRuntimeError("Invalid Input");
                }

                if (purpose == 0) {
                    mf.setValue(memoryAddress, text, toggle, row);
                } else if (purpose == 1) {
                    if (Integer.parseInt(text, 16) >= 0x3000 && Integer.parseInt(text, 16) <= 0x4FF8 && Integer.parseInt(text, 16) % 0x8 == 0) {
                        int cell = (Integer.parseInt(text, 16) - 0x3000) / 0x8;
                        mf.jumpToMemory(cell);
                    } else {
                        Errors.addRuntimeError("Invalid memory location.");
                    }
                }
                dialog.dispose();
            }
        });
        valuePanel.add(send);

        dialog.getContentPane().add(valuePanel, 0);
        dialog.setSize(200, 100);
        dialog.setLocationRelativeTo(mf);
    }

    public void showDialog() {
        dialog.setVisible(true);
    }

    public void createErrorDialog(String message) {
        errorDialog = new JDialog(mf, "ERROR", false);
        JLabel errorMessage = new JLabel();
        errorMessage.setText(message);
        JPanel errorPanel = new JPanel();
        errorPanel.add(errorMessage);
        JButton done = new JButton("OK");
        done.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                errorDialog.dispose();
            }
        });
        errorPanel.add(done);

        errorDialog.getContentPane().add(errorPanel, 0);
        errorDialog.setSize(200, 100);
        errorDialog.setLocationRelativeTo(mf);
    }

    public void showErrorMessage() {
        errorDialog.setVisible(true);
    }
}
