package WorkForm;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Arrays;
import java.util.Vector;

public class StuffUserGUI {

    static final String[] USER_HEADER = {"用户名", "密码", "购买票数"};
    static final String[] TRAIN_HEADER = {"车次", "出发地", "目的地", "发车时间", "行程"};
    private DefaultTableModel userTableModel;
    private DefaultTableModel trainTableModel;
    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private JPanel userManagePanel;
    private JPanel trainManagePanel;
    private JFrame window;

    public StuffUserGUI() {
        window = new JFrame("管理页面");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setContentPane(tabbedPane1);


        tabbedPane1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                System.out.println("change Listener 触发");
                //todo: 链接至刷新列表事件
            }
        });
    }

    public static void main(String[] args) {
        var sug = new StuffUserGUI();
        sug.run();
    }

    public void run() {
        window.pack();
        Dimension frameSize = window.getSize();
        frameSize.width *= 4;
        frameSize.height *= 2;
        window.setSize(frameSize);
        WindowTools.moveToCenter(window);
        window.setVisible(true);

    }

    public void drawUserList(Vector<Vector<Object>> vectors) {
        userTableModel = new DefaultTableModel(vectors, new Vector<>(Arrays.asList(USER_HEADER))) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        var table = new JTable(userTableModel);
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        userManagePanel.removeAll();
        userManagePanel.add(new JScrollPane(table));
    }

    public void drawTrainList(Vector<Vector<Object>> vectors) {
        trainTableModel = new DefaultTableModel(vectors, new Vector<>(Arrays.asList(TRAIN_HEADER))) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        };

        var table = new JTable(trainTableModel);
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        trainManagePanel.removeAll();
        trainManagePanel.add(new JScrollPane(table));
    }

    public void updateUserTable(Vector<Vector<Object>> vectors) {

        userTableModel.setDataVector(vectors, new Vector<>(Arrays.asList(USER_HEADER)));

    }

    public void updateTrainTable(Vector<Vector<Object>> vectors) {
        trainTableModel.setDataVector(vectors, new Vector<>(Arrays.asList(TRAIN_HEADER)));
    }
}
