package WorkForm;

import javax.swing.*;
import java.awt.*;

public class ImagePanel extends JPanel {

    private Image img;

    public ImagePanel(Image img) {
        this.img = img;
        Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
        setSize(size);
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setLayout(null);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Dimension size = this.getParent().getSize();
        // g.drawImage(img, 0, 0, this);//此方法不能改变图片大小
        g.drawImage(img, 0, 0, size.width, size.height, this);// 此方法中的图片大小可随屏幕的改变而改变
    }

}