package WorkForm;

import javax.swing.*;
import java.awt.*;

public abstract class WindowTools {
    public static void moveToCenter(Window window) {

        //设置窗口

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = window.getSize();
//        frameSize.width *= 4;
//        frameSize.height *= 2;
//        window.setSize(frameSize);
        if (frameSize.height > screenSize.height)
            frameSize.height = screenSize.height;
        if (frameSize.width > screenSize.width)
            frameSize.width = screenSize.width;
        window.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    }

    public static void scaleWindow(JFrame window, double x, double y) {
        Dimension frameSize = window.getSize();
        frameSize.width *= x;
        frameSize.height *= y;
        window.setSize(frameSize);
    }
}
