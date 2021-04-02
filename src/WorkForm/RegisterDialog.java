package WorkForm;

import DataType.User;
import Logics.DataBase;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import javax.swing.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.Arrays;

public class RegisterDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JPasswordField passwordField2;

    public RegisterDialog() {
        setTitle("注册");
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
        RegisterDialog dialog = new RegisterDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    private void onOK() {
        var name = textField1.getText();
        if (name.length() < 3 || name.length() > 20) {
            JOptionPane.showMessageDialog(this, "用户名" + (name.length() < 3 ? "过短" : "过长"));
            return;
        }
        var pass = passwordField1.getPassword();
        if (pass.length < 6 || pass.length > 20) {
            JOptionPane.showMessageDialog(this, "密码" + (pass.length < 6 ? "过短" : "过长"));
            return;
        }
        if (!Arrays.equals(pass, passwordField2.getPassword())) {
            JOptionPane.showMessageDialog(this, "两次输入的密码不一致");
            return;
        }
        try {
            User u = new User(name, String.valueOf(pass));
            DataBase.insertUser(u);
            dispose();
        } catch (MySQLIntegrityConstraintViolationException e) {
            JOptionPane.showMessageDialog(this, "用户名已存在");
        } catch (IllegalArgumentException | SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, e.toString());
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

}
