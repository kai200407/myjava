package com.example.ui;

import com.example.dao.AnnouncementDAO;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AnnouncementViewFrame extends JFrame {
    private JTextField keywordField;
    private JTextField dateField;
    private DefaultTableModel tableModel;
    private AnnouncementDAO announcementDAO;

    public AnnouncementViewFrame() {
        // 在 AnnouncementViewFrame 的构造方法中顶部面板添加按钮（参考之前已有的 rightPanel）
        RoundedButton deviceManageButton = new RoundedButton("设备管理");
        deviceManageButton.addActionListener(e -> new DeviceFrame("user").setVisible(true));

        // 将 deviceManageButton 添加到 AnnouncementViewFrame 顶部右侧按钮区
        rightPanel.add(deviceManageButton);

        announcementDAO = new AnnouncementDAO();
        setTitle("查看公告（用户）");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        BackgroundPanel mainPanel = new BackgroundPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(mainPanel);

        // 顶部：标题 + 切换登录
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("公告查看", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLabel.setForeground(new Color(50, 50, 50));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.add(titleLabel, BorderLayout.CENTER);

        RoundedButton switchUserButton = new RoundedButton("切换登录");
        switchUserButton.setPreferredSize(new Dimension(100, 30));
        switchUserButton.addActionListener(e -> switchLogin());
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
        rightPanel.add(switchUserButton);
        topPanel.add(rightPanel, BorderLayout.EAST);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setOpaque(false);

        TitledBorder tb = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(150, 150, 150), 1),
                "查看和搜索公告",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 16),
                new Color(60, 60, 60));
        centerPanel.setBorder(tb);

        JPanel topSearchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topSearchPanel.setOpaque(false);

        JLabel keywordLbl = new JLabel("关键字：");
        keywordLbl.setFont(new Font("SansSerif", Font.PLAIN, 14));
        topSearchPanel.add(keywordLbl);

        keywordField = createStyledTextField("输入标题或内容关键字");
        topSearchPanel.add(keywordField);

        JLabel dateLbl = new JLabel("发布日期(模糊)：");
        dateLbl.setFont(new Font("SansSerif", Font.PLAIN, 14));
        topSearchPanel.add(dateLbl);

        dateField = createStyledTextField("如：2024-12");
        dateField.setToolTipText("可输入部分日期（如2024-12）查找该月公告");
        topSearchPanel.add(dateField);

        RoundedButton searchButton = new RoundedButton("搜索");
        searchButton.addActionListener(e -> searchAnnouncements());
        topSearchPanel.add(searchButton);

        centerPanel.add(topSearchPanel, BorderLayout.NORTH);

        String[] columns = { "标题", "内容", "发布日期" };
        tableModel = new DefaultTableModel(columns, 0);
        JTable announcementTable = new JTable(tableModel);
        announcementTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        announcementTable.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(announcementTable);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // 初始显示所有公告
        searchAnnouncements();
    }

    private JTextField createStyledTextField(String tooltip) {
        JTextField field = new JTextField(10);
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(150, 150, 150)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        field.setToolTipText(tooltip);
        return field;
    }

    private void searchAnnouncements() {
        String keyword = keywordField.getText().trim();
        String date = dateField.getText().trim();
        List<String[]> results = announcementDAO.searchAnnouncements(keyword, date);
        tableModel.setRowCount(0);
        for (String[] row : results) {
            tableModel.addRow(row);
        }
    }

    private void switchLogin() {
        new LoginFrame().setVisible(true);
        dispose();
    }
}
