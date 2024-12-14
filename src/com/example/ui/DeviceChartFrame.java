package com.example.ui;

import com.example.dao.DeviceDAO;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class DeviceChartFrame extends JFrame {
    private DeviceDAO deviceDAO;

    public DeviceChartFrame() {
        deviceDAO = new DeviceDAO();
        setTitle("设备状态统计");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        add(new ChartPanel());
    }

    class ChartPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Map<String, Integer> statusCount = deviceDAO.getStatusCount();
            if (statusCount.isEmpty()) {
                g.drawString("无数据", 10, 20);
                return;
            }

            // 简单的条形图示例
            int startX = 50;
            int startY = 50;
            int barHeight = 30;
            int gap = 20;

            // 找最大值以便计算比例
            int max = statusCount.values().stream().max(Integer::compareTo).orElse(1);
            int usableWidth = getWidth() - 100; // 条形图可用宽度
            int i = 0;

            g.setFont(new Font("SansSerif", Font.PLAIN, 14));
            for (Map.Entry<String, Integer> e : statusCount.entrySet()) {
                String status = e.getKey();
                int count = e.getValue();
                int barWidth = (int) ((count / (double) max) * usableWidth);

                int y = startY + i * (barHeight + gap);
                g.setColor(new Color(59, 89, 182));
                g.fillRect(startX, y, barWidth, barHeight);

                g.setColor(Color.BLACK);
                g.drawString(status + ": " + count, startX + barWidth + 10, y + barHeight / 2 + 5);

                i++;
            }

            // 标题
            g.setFont(new Font("SansSerif", Font.BOLD, 16));
            g.drawString("设备状态统计", getWidth() / 2 - 50, 30);
        }
    }
}
