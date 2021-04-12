package WorkForm;

import DataType.Train;
import Logics.DataBase;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import javax.swing.text.NumberFormatter;
import java.awt.event.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Vector;

import static Logics.DataBase.DATE_FORMAT;

public class CreateTrainDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox<String> fromCombo;
    private JComboBox<String> toCombo;
    private JFormattedTextField trainName;
    private JFormattedTextField timeCost;
    private JFormattedTextField startTime;
    private JFormattedTextField moneyCost;

    public CreateTrainDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

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

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public static void main(String[] args) {
        CreateTrainDialog dialog = new CreateTrainDialog();
        dialog.setPlaces(new Vector<>(Arrays.asList(DataBase.getPlaces())));
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

    public void setPlaces(String[] vector) {
        for (String str : vector) {
            fromCombo.addItem(str);
            toCombo.addItem(str);
        }

    }

    private void onOK() {
        try {
            if (fromCombo.getSelectedIndex() == toCombo.getSelectedIndex()) {
                JOptionPane.showMessageDialog(this, "禁止原地tp");
                return;
            }
            Calendar c = Calendar.getInstance();
            c.setTime((java.util.Date) startTime.getValue());
            double timeCostValueD = (Double) timeCost.getValue();
            int timeCostValue = (int) timeCostValueD;
            DataBase.insertTrain(new Train(
                    0,
                    (String) (trainName.getValue()),
                    fromCombo.getSelectedIndex() + 1,
                    toCombo.getSelectedIndex() + 1,
                    c,
                    timeCostValue,
                    (Double) moneyCost.getValue()
            ));
            dispose();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    private void createUIComponents() {

        SimpleDateFormat tFmt = new SimpleDateFormat(DATE_FORMAT);
        startTime = new JFormattedTextField(tFmt);
        Calendar calendar = Calendar.getInstance();
        startTime.setValue(calendar.getTime());
        NumberFormatter nFmt=new NumberFormatter();
        nFmt.setMinimum(0);
        moneyCost = new JFormattedTextField(nFmt);
        timeCost = new JFormattedTextField(nFmt);
        try {
            MaskFormatter fm = new MaskFormatter("H####");
            trainName = new JFormattedTextField(fm);
            trainName.setValue("D2333");

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
