package WorkForm;

import javax.swing.*;

public class DefaultUserGUI {
    private JFrame window;
    private JPanel basePanel;

    public DefaultUserGUI() {
        window = new JFrame("title");
        window.setContentPane(basePanel);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        var s = new DefaultUserGUI();
        s.run();
    }

    public void run() {
        window.pack();
        window.setVisible(true);
    }
}
