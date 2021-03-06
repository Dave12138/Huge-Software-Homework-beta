package WorkForm;

import DataType.User;
import Logics.DataBase;
import exception.MyCustomMessageException;

import javax.swing.*;
import java.awt.event.*;
import java.util.Arrays;

public class UserChangePasswordDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JPasswordField oldField;
    private JPasswordField repeatField;
    private JPasswordField newField;
    private User user;

    public UserChangePasswordDialog(User u) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        user = u;
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


    private void onOK() {
        if (!user.getPassword().equals(String.valueOf(oldField.getPassword()))) {
            JOptionPane.showMessageDialog(this, "旧密码不正确");
            return;
        }

        char[] pass = newField.getPassword();
        if (pass.length < 6 || pass.length > 20) {
            JOptionPane.showMessageDialog(this, "密码" + (pass.length < 6 ? "过短" : "过长"));
            return;
        }
        if (!Arrays.equals(pass, repeatField.getPassword())) {
            JOptionPane.showMessageDialog(this, "两次输入的密码不一致");
            return;
        }
        try {
            DataBase.updateUserPassword(user.getName(), pass);
            user.setPassword(pass);

            dispose();
        } catch (MyCustomMessageException e) {
            e.printStackTrace();
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
