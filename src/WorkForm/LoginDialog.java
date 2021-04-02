package WorkForm;

import DataType.User;
import Logics.DataBase;

import javax.swing.*;
import java.awt.event.*;

import static WorkForm.WindowTools.moveToCenter;

public class LoginDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField nameField;
    private JPasswordField passwordField;
    private JButton registerButton;
    private User u;

    public LoginDialog() {
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
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onRegister();
            }
        });
    }

    public static void main(String[] args) {
        LoginDialog dialog = new LoginDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    private void onOK() {
        u = DataBase.getUserByName(getUserName());
        if (u == null || !u.getPassword().equals(String.valueOf(getPassword()))) {

            System.out.printf("u.getPassword().equals(String.valueOf(getPassword(%s)))\n",String.valueOf(getPassword()));
            System.out.printf("u == %s",u==null?"null":"user");
            JOptionPane.showMessageDialog(this, "用户名或密码错误");
            u = null;
            return;
        }

        dispose();
    }

    private void onCancel() {

        dispose();
        System.exit(0);
    }

    private void onRegister() {
        RegisterDialog registerDialog = new RegisterDialog();
        registerDialog.pack();
        moveToCenter(registerDialog);
        registerDialog.setVisible(true);
    }

    public String getUserName() {
        return nameField.getText();
    }

    public char[] getPassword() {
        return passwordField.getPassword();
    }

    public User getUser() {
        return u;
    }
}
