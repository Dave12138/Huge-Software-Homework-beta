package WorkForm;

import javax.swing.*;

public class DefaultUserGUI {
    private JFrame window;
    private JPanel basePanel;
    private JButton buttonDestroyTicket;
    private JButton buttonBuyTicket;
    private JButton buttonChangePassword;
    private JPanel shadePanel;

    public DefaultUserGUI() {
        window = new JFrame("购票系统lite");
        window.setContentPane(basePanel);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        buttonDestroyTicket.setContentAreaFilled(false);
        buttonBuyTicket.setContentAreaFilled(false);
        buttonChangePassword.setContentAreaFilled(false);
    }

    public static void main(String[] args) {
        var s = new DefaultUserGUI();
        s.run();
    }

    public void run() {
        window.pack();
        WindowTools.moveToCenter(window);
        window.setVisible(true);
    }

    private void createUIComponents() {
        basePanel = new ImagePanel(new ImageIcon("image/MainMenuPicture.png").getImage());
        shadePanel = new ImagePanel(new ImageIcon("image/MainMenuShade.png").getImage());
    }
}
