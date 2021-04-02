package WorkForm;

import javax.swing.*;

public class DefaultUserGUI extends JFrame {
    private JPanel basePanel;
    private JButton buttonDestroyTicket;
    private JButton buttonBuyTicket;
    private JButton buttonChangePassword;
    private JPanel shadePanel;

    public DefaultUserGUI() {
        super("购票系统lite");
        setContentPane(basePanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        buttonDestroyTicket.setContentAreaFilled(false);
        buttonBuyTicket.setContentAreaFilled(false);
        buttonChangePassword.setContentAreaFilled(false);
    }

    public static void main(String[] args) {
        var s = new DefaultUserGUI();
        s.run();
    }

    public void run() {
        pack();
        WindowTools.moveToCenter(this);
        setVisible(true);
    }

    private void createUIComponents() {
        basePanel = new ImagePanel(new ImageIcon("image/MainMenuPicture.png").getImage());
        shadePanel = new ImagePanel(new ImageIcon("image/MainMenuShade.png").getImage());
    }
}
