package WorkForm;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.Arrays;
import java.util.Vector;

public class TicketTableDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JScrollPane tablePanel;
    private Vector<Vector<Object>> tableVector;
    private String target;
    private JTable ticketTable;

    public TicketTableDialog(Vector<Vector<Object>> tableVector) {
        this.tableVector = tableVector;
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
        String[] dat1 = {"a", "b", "c", "d", "e"};
        String[] dat2 = {"f", "g", "h", "i", "j"};
        var vvv = new Vector<Vector<Object>>();
        vvv.add(new Vector<>(Arrays.asList(dat1)));
        vvv.add(new Vector<>(Arrays.asList(dat2)));
        TicketTableDialog dialog = new TicketTableDialog(vvv);
        dialog.pack();
        dialog.setVisible(true);
        String st = dialog.getSelectTarget();
        if (st == null) {
            st = "未选择";
        }
        JOptionPane.showMessageDialog(null, st);
        System.exit(0);
    }

    private void onOK() {
        int row = ticketTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(null, "未选择车票");
            return;
        }

        target = ticketTable.getValueAt(row, 0).toString();
        dispose();
    }

    private void onCancel() {
        target = null;
        dispose();
    }

    private void createUIComponents() {
        String[] header = {"车次", "出发地", "目的地", "发车时间", "行程时长"};
        DefaultTableModel model = new DefaultTableModel(tableVector, new Vector<>(Arrays.asList(header))) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        ticketTable = new JTable(model);
        ticketTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ticketTable.getTableHeader().setReorderingAllowed(false);
        tablePanel = new JScrollPane(ticketTable);
    }

    public String getSelectTarget() {
        return target;
    }
}
