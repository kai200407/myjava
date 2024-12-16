package release.ui;

import javax.swing.*;
import java.awt.*;

public class BackgroundPanel extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // 绘制渐变背景
        Graphics2D g2d = (Graphics2D) g.create();
        int width = getWidth();
        int height = getHeight();
        
        Color startColor = new Color(225, 238, 255);
        Color endColor = new Color(200, 220, 255);
        GradientPaint gradient = new GradientPaint(0, 0, startColor, 0, height, endColor);
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, width, height);
        g2d.dispose();
    }
}
