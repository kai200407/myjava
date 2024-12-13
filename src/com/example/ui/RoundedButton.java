package com.example.ui;

import javax.swing.*;
import java.awt.*;

public class RoundedButton extends JButton {
    private Color hoverBackgroundColor;
    private Color pressedBackgroundColor;

    public RoundedButton(String text) {
        super(text);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setBorderPainted(false);
        setFont(new Font("SansSerif", Font.BOLD, 14));
        setBackground(new Color(59, 89, 182));
        setForeground(Color.WHITE);
        hoverBackgroundColor = new Color(79, 109, 202);
        pressedBackgroundColor = new Color(39, 69, 162);

        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                setBackground(hoverBackgroundColor);
                repaint();
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                setBackground(new Color(59, 89, 182));
                repaint();
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                setBackground(pressedBackgroundColor);
                repaint();
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                setBackground(hoverBackgroundColor);
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        // 圆角背景
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(getBackground());
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
        g2.dispose();
        super.paintComponent(g);
    }
}

