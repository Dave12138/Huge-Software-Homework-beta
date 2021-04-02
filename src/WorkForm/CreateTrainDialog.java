package WorkForm;

import javax.swing.*;
import java.awt.event.*;
import java.util.Calendar;
import java.util.Vector;

public class CreateTrainDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox<String> fromCombo;
    private JComboBox<String> toCombo;
    private JFormattedTextField trainName;
    private JComboBox<Integer> yearCombo;
    private JComboBox<Integer> monCombo;
    private JComboBox<Integer> dayCombo;
    private JComboBox<Integer> startHour;
    private JComboBox<Integer> startMinute;
    private JSpinner timeCostSpinner;

    public CreateTrainDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        //todo:trainName 输入控制
        //todo:timeCostSpinner下限
        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < 4; i++) {
            yearCombo.addItem(calendar.get(Calendar.YEAR) + 2 - i);
        }
        for (int i = 1; i < 13; i++) {
            monCombo.addItem(i);
        }
        for (int i = 1; i < 32; i++) {
            dayCombo.addItem(i);
        }
        for (int i = 0; i < 24; i++) {
            startHour.addItem(i);
        }
        for (int i = 0; i < 60; i++) {
            startMinute.addItem(i);
        }
        yearCombo.setSelectedItem(calendar.get(Calendar.YEAR));
        monCombo.setSelectedItem(calendar.get(Calendar.MONTH) + 1);
        dayCombo.setSelectedItem(calendar.get(Calendar.DATE));
        startHour.setSelectedItem(calendar.get(Calendar.HOUR_OF_DAY));
        startMinute.setSelectedItem(calendar.get(Calendar.MINUTE));

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public static void main(String[] args) {
        CreateTrainDialog dialog = new CreateTrainDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    public void setPlaces(Vector<String> vector) {
        for (String str : vector) {
            fromCombo.addItem(str);
            toCombo.addItem(str);
        }
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
