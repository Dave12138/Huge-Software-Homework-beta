package WorkForm;

import DataType.User;
import Logics.DataBase;
import exception.MyCustomMessageException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Vector;

public class DefaultUserGUI extends JFrame {
    private JPanel basePanel;
    private JButton buttonDestroyTicket;
    private JButton buttonBuyTicket;
    private JButton buttonChangePassword;
    private JPanel shadePanel;
    private JScrollPane tablePanel;
    private JButton rechargeButton;
    private JLabel moneyLabel;
    private User user;
    private JTable ticketTable;
    private DefaultTableModel trainTableModel;


    public DefaultUserGUI() {
        super("购票系统lite");
        setContentPane(basePanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        buttonBuyTicket.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TicketTableDialog dialog = new TicketTableDialog();
                dialog.pack();
                WindowTools.moveToCenter(dialog);
                dialog.setVisible(true);
                String s = dialog.getSelectTarget();
                if (s == null) {
                    return;
                }
                if(!user.couldAfford((int) dialog.getCost())){
                    return;
                }

                try {

                    DataBase.insertBuyTicket(user.getName(), Integer.parseInt(s));
                    user.consume((int) dialog.getCost());
                    DataBase.updateUserMoney(user.getName(),user.getMoney());
                    updateTrainList();
                    moneyLabel.setText(String.valueOf(user.getMoney()));
                } catch (SQLException | ClassNotFoundException | MyCustomMessageException ex) {
                    ex.printStackTrace();
                }
            }
        });
        buttonChangePassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UserChangePasswordDialog dialog = new UserChangePasswordDialog(user);
                dialog.pack();
                WindowTools.moveToCenter(dialog);
                dialog.setVisible(true);
            }
        });
        buttonDestroyTicket.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int row = ticketTable.getSelectedRow();

                if (row == -1) {
                    JOptionPane.showMessageDialog(null, "未选择车票");
                    return;
                }
                try {
                    double cost = Double.parseDouble(ticketTable.getValueAt(row, 6).toString());
                    DataBase.removeBoughtTicket(user.getName(),Integer.decode(ticketTable.getValueAt(row,0).toString()));
                    user.recharge((int) cost);
                    DataBase.updateUserMoney(user.getName(),user.getMoney());
                    updateTrainList();
                    moneyLabel.setText(String.valueOf(user.getMoney()));
                } catch (SQLException | ClassNotFoundException | MyCustomMessageException ex) {
                    ex.printStackTrace();
                }
            }
        });
        rechargeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RechargeDialog dialog = new RechargeDialog(user);
                dialog.pack();
                WindowTools.moveToCenter(dialog);
                dialog.setVisible(true);
                moneyLabel.setText(String.valueOf(user.getMoney()));
            }
        });
    }

    private void updateTrainList() {

        trainTableModel.setDataVector(DataBase.trainsToTable(DataBase.getBoughtTicket(user.getName())), new Vector<>(Arrays.asList(StuffUserGUI.TRAIN_HEADER)));
    }


    public void setLoginUser(User user) {
        this.user = user;
        updateTrainList();
        moneyLabel.setText(String.valueOf(user.getMoney()));
    }

    public void run() {
        pack();
        WindowTools.moveToCenter(this);
        setVisible(true);
    }

    private void createUIComponents() {
        basePanel = new ImagePanel(new ImageIcon("image/MainMenuPicture.png").getImage());
        shadePanel = new ImagePanel(new ImageIcon("image/MainMenuShade.png").getImage());

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
}
