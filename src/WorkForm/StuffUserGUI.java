package WorkForm;

import DataType.Train;
import DataType.User;
import Logics.DataBase;
import exception.MyCustomMessageException;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Vector;

public class StuffUserGUI extends JFrame {

    static final String[] USER_HEADER = {"用户名", "密码", "有管理员权限"};
    static final String[] TRAIN_HEADER = {"uid", "车次", "出发地", "目的地", "发车时间", "行程", "票价"};
    private DefaultTableModel userTableModel;
    private DefaultTableModel trainTableModel;
    private JTabbedPane mainTabbedPane;
    private JPanel basePanel;
    private JScrollPane userManagePanel;
    private JScrollPane trainManagePanel;
    private JButton userDeleteButton;
    private JButton trainAddButton;
    private JButton trainRemoveButton;
    private JTextField trainSearch;
    private JTextField userSearch;

    public StuffUserGUI() {
        super("管理页面");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(basePanel);
        mainTabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                System.out.println("change Listener 触发");
                //todo: 链接至刷新列表事件
                User[] us = DataBase.getUsers();
                Train[] tr = DataBase.getTrains();
                if (us != null) {
                    updateUserTable(DataBase.usersToTable(us));
                }
                if (tr != null) {
                    updateTrainTable(DataBase.trainsToTable(tr));
                }
            }
        });
        userSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {

                User[] us = DataBase.getUsers(userSearch.getText());
                updateUserTable(DataBase.usersToTable(us));
            }

            @Override
            public void removeUpdate(DocumentEvent e) {

                User[] us = DataBase.getUsers(userSearch.getText());
                updateUserTable(DataBase.usersToTable(us));
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });
        trainAddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CreateTrainDialog dialog = new CreateTrainDialog();
                dialog.setPlaces(DataBase.getPlaces());
                dialog.pack();
                WindowTools.moveToCenter(dialog);
                dialog.setVisible(true);
            }
        });
    }

    public static void main(String[] args) {
        var sug = new StuffUserGUI();
        sug.run();
    }

    public void run() {
        pack();
        WindowTools.scaleWindow(this, 4, 2);
        WindowTools.moveToCenter(this);
        setVisible(true);
    }

    public void drawUserList(Vector<Vector<Object>> vectors) {
        userTableModel = new DefaultTableModel(vectors, new Vector<>(Arrays.asList(USER_HEADER))) {
            public boolean isCellEditable(int row, int column) {
                return column > 0;
            }
        };
        userTableModel.addTableModelListener(e -> {
            int type = e.getType();//获取事件类型(增、删、改等)
            int row = e.getFirstRow();//获取触发事件的行索引
            int column = e.getColumn();//获取触发事件的列索引
            if (type == TableModelEvent.UPDATE && row > -1) {
                System.out.println("此事件是由\"修改\"触发,在" + row + "行" + column + "列");
                if (column == 1) {
                    String nPass = (String) userTableModel.getValueAt(row, column);
                    if (nPass.length() < 6 || nPass.length() > 20) {
                        JOptionPane.showMessageDialog(this, "密码" + (nPass.length() < 6 ? "过短" : "过长"));
                    } else {
                        try {
                            DataBase.updateUserPassword((String) userTableModel.getValueAt(row, 0), nPass.toCharArray());
                        } catch (MyCustomMessageException ex) {
                            ex.showDialog();
                        }
                    }
                } else if (column == 2) {
                    try {
                        boolean isAdmin = Boolean.parseBoolean((String) userTableModel.getValueAt(row, column));
                        DataBase.updateUserPower((String) userTableModel.getValueAt(row, 0), isAdmin);
                    } catch (RuntimeException ignored) {

                    } catch (MyCustomMessageException ex) {
                        ex.showDialog();
                    }
                }
                User[] us = DataBase.getUsers(userSearch.getText());
                updateUserTable(DataBase.usersToTable(us));
            }
        });
        var table = new JTable(userTableModel);
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);

        userDeleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                if (row == -1) {
                    JOptionPane.showMessageDialog(StuffUserGUI.this, "没有选择用户");
                    return;
                }
                try {
                    DataBase.removeUser(table.getValueAt(row, 0).toString());
                } catch (SQLException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }

            }
        });
//        userManagePanel.removeAll();
        userManagePanel.setViewportView(table);
    }

    public void drawTrainList(Vector<Vector<Object>> vectors) {
        trainTableModel = new DefaultTableModel(vectors, new Vector<>(Arrays.asList(TRAIN_HEADER))) {
            public boolean isCellEditable(int row, int column) {
                return column > 0;
            }
        };

        var table = new JTable(trainTableModel);
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
//        trainManagePanel.removeAll();
        trainManagePanel.setViewportView(table);

        trainRemoveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int row = table.getSelectedRow();
                if (row == -1) {
                    JOptionPane.showMessageDialog(StuffUserGUI.this, "没有选择车次");
                    return;
                }
                try {
                    DataBase.removeTrain(Integer.parseInt(table.getValueAt(row, 0).toString()));
                } catch (SQLException | ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public void updateUserTable(Vector<Vector<Object>> vectors) {

        userTableModel.setDataVector(vectors, new Vector<>(Arrays.asList(USER_HEADER)));

    }

    public void updateTrainTable(Vector<Vector<Object>> vectors) {
        trainTableModel.setDataVector(vectors, new Vector<>(Arrays.asList(TRAIN_HEADER)));
    }

    private void createUIComponents() {
        basePanel = new ImagePanel("image/86541424_p0.jpg");
    }
}
