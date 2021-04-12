package WorkForm;

import DataType.User;
import Logics.DataBase;
import exception.MyCustomMessageException;

import javax.swing.*;
import java.awt.event.*;

public class RechargeDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JButton 氪100Button;
    private JButton 氪20Button;
    private User user;

    public RechargeDialog(User user) {
        setContentPane(contentPane);
        this.user = user;
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK(50);
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
        氪100Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onOK(100);
            }
        });
        氪20Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                onOK(20);
            }
        });
    }


    private void onOK(int val) {
        user.recharge(val);
        try {
            DataBase.updateUserMoney(user.getName(), user.getMoney());
            dispose();
        } catch (MyCustomMessageException e) {
            e.printStackTrace();
            try {
                user.consume(val);
            } catch (MyCustomMessageException ignore) {
            }
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
