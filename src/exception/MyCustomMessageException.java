package exception;

import javax.swing.*;

public class MyCustomMessageException extends Exception {
    public MyCustomMessageException(String s) {
        super(s);
    }

    public void showDialog() {
        JOptionPane.showMessageDialog(null, super.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
