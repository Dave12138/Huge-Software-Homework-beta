package WorkForm;

import Logics.DataBase;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Vector;

public class TicketTableDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JScrollPane tablePanel;
    private JComboBox<String> comboBox1;
    private JComboBox<String> comboBox2;
    private JComboBox<Integer> yearCombo;
    private JComboBox<Integer> monCombo;
    private JComboBox<Integer> dayCombo;
    private DefaultTableModel trainTableModel;
    private String target;
    private JTable ticketTable;
    private double cost;

    public TicketTableDialog() {
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
        setPlaces(DataBase.getPlaces());
        {
            Calendar pointer = Calendar.getInstance();
            int preYear = -1;
            int preMon = -1;
            for (int i = 0; i < 14; i++) {
                if (preYear != pointer.get(Calendar.YEAR)) {
                    preYear = pointer.get(Calendar.YEAR);
                    yearCombo.addItem(preYear);
                }
                if (preMon != pointer.get(Calendar.MONTH)) {
                    preMon = pointer.get(Calendar.MONTH);
                    monCombo.addItem(preMon + 1);
                }
                dayCombo.addItem(pointer.get(Calendar.DAY_OF_MONTH) + 1);
                pointer.add(Calendar.DATE, 1);
            }
        }
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
        ItemListener il = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    Calendar today = Calendar.getInstance();
                    today.set((Integer) yearCombo.getSelectedItem(), (Integer) monCombo.getSelectedItem() - 1, (Integer) dayCombo.getSelectedItem());
//                    System.out.println(DataBase.formatCalendar(today));
                    updateTrainList(DataBase.trainsToTable(DataBase.getTrains("`from`=" + (comboBox1.getSelectedIndex() + 1) + " and `to`=" + (comboBox2.getSelectedIndex() + 1), today)));
                }
            }
        };
        comboBox1.addItemListener(il);
        comboBox2.addItemListener(il);
        dayCombo.addItemListener(il);
        monCombo.addItemListener(il);
        yearCombo.addItemListener(il);
        cost = 0;
    }


    private void updateTrainList(Vector<Vector<Object>> vector) {

        trainTableModel.setDataVector(vector, new Vector<>(Arrays.asList(StuffUserGUI.TRAIN_HEADER)));
    }

    private void setPlaces(String[] vector) {
        for (String str : vector) {
            comboBox1.addItem(str);
            comboBox2.addItem(str);
        }
    }

    private void onOK() {
        int row = ticketTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(null, "未选择车票");
            return;
        }
        String timeStr = ticketTable.getValueAt(row, 4).toString();
        SimpleDateFormat fmt = new SimpleDateFormat(DataBase.DATE_FORMAT);
        Calendar startTime = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, 10);
        try {
            startTime.setTime(fmt.parse(timeStr));
            if (!now.before(startTime)) {
                JOptionPane.showMessageDialog(this, "停止售票：即将到达发车时间");
                return;
            }
            target = ticketTable.getValueAt(row, 0).toString();
            cost = Double.parseDouble(ticketTable.getValueAt(row, 6).toString());
            dispose();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void onCancel() {
        target = null;
        dispose();
    }

    private void createUIComponents() {
        trainTableModel = new DefaultTableModel(null, new Vector<>(Arrays.asList(StuffUserGUI.TRAIN_HEADER))) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        ticketTable = new JTable(trainTableModel);
        ticketTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ticketTable.getTableHeader().setReorderingAllowed(false);
        tablePanel = new JScrollPane(ticketTable);
    }

    public String getSelectTarget() {
        return target;
    }

    public double getCost() {
        return cost;
    }
}
