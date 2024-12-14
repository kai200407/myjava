package com.example.ui;

import com.example.dao.AnnouncementDAO;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.util.List;

public class AnnouncementFrame extends JFrame {
    private JTextField titleField;
    private JTextArea contentArea;
    private JTextField dateField;
    private AnnouncementDAO announcementDAO;
    private JTextField keywordField;
    private JTextField searchDateField;
    private DefaultTableModel tableModel;

    public AnnouncementFrame() {
        // 示例：在 AnnouncementFrame 的构造方法中顶部面板添加按钮（参考之前已有的 rightPanel）

        RoundedButton deviceManageButton = new RoundedButton("设备管理");
        deviceManageButton.addActionListener(e -> new DeviceFrame("admin").setVisible(true));

        // 将 deviceManageButton 添加到 AnnouncementFrame 顶部右侧按钮区
        rightPanel.add(deviceManageButton);

        announcementDAO = new AnnouncementDAO();
        setTitle("公告管理（管理员）");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        BackgroundPanel mainPanel = new BackgroundPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(mainPanel);

        // 顶部：标题 + 切换登录
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("公告管理中心", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
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

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setFont(new Font("SansSerif", Font.PLAIN, 14));

        // 发布公告面板
        JPanel publishPanel = createPublishPanel();
        tabbedPane.addTab("发布公告", publishPanel);

        // 查看公告面板（管理员可搜索）
        JPanel viewPanel = createViewPanel();
        tabbedPane.addTab("查看公告", viewPanel);

        mainPanel.add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createPublishPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        TitledBorder tb = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(150, 150, 150), 1),
                "发布公告",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 16),
                new Color(60, 60, 60));
        panel.setBorder(tb);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL; // 让输入框可以横向拉伸

        // 标题
        JLabel titleLabel = new JLabel("标题：");
        titleLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        titleField = createStyledTextField("示例：服务器维护通知");
        titleField.setColumns(30); // 扩大宽度

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        panel.add(titleLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(titleField, gbc);

        // 内容
        JLabel contentLabel = new JLabel("内容：");
        contentLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));

        contentArea = new JTextArea(8, 30); // 行数8，列数30，使区域更大
        contentArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(150, 150, 150)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        contentArea.setToolTipText("请输入公告正文内容。示例：本周五22:00至次日1:00进行服务器维护。");
        JScrollPane contentScroll = new JScrollPane(contentArea);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        panel.add(contentLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(contentScroll, gbc);

        // 发布日期
        JLabel dateLabel = new JLabel("发布日期(YYYY-MM-DD)：");
        dateLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        dateField = createStyledTextField("示例：2024-12-01");
        dateField.setColumns(30); // 扩大宽度
        dateField.setText(LocalDate.now().toString());

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        panel.add(dateLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(dateField, gbc);

        // 发布按钮行
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);
        RoundedButton publishButton = new RoundedButton("发布公告");
        publishButton.addActionListener(this::handlePublish);

        buttonPanel.add(publishButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(buttonPanel, gbc);

        return panel;
    }

    private JPanel createViewPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setOpaque(false);

        TitledBorder tb = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(150, 150, 150), 1),
                "查看和搜索公告",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 16),
                new Color(60, 60, 60));
        panel.setBorder(tb);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.setOpaque(false);

        JLabel keywordLbl = new JLabel("关键字：");
        keywordLbl.setFont(new Font("SansSerif", Font.PLAIN, 14));
        topPanel.add(keywordLbl);

        keywordField = createStyledTextField("输入标题或内容关键字");
        topPanel.add(keywordField);

        JLabel dateLbl = new JLabel("发布日期(模糊)：");
        dateLbl.setFont(new Font("SansSerif", Font.PLAIN, 14));
        topPanel.add(dateLbl);

        searchDateField = createStyledTextField("如：2024-12");
        searchDateField.setToolTipText("可输入部分日期（如2024-12）查找该月公告");
        topPanel.add(searchDateField);

        RoundedButton searchButton = new RoundedButton("搜索");
        searchButton.addActionListener(e -> searchAnnouncements());
        topPanel.add(searchButton);

        panel.add(topPanel, BorderLayout.NORTH);

        String[] columns = { "标题", "内容", "发布日期" };
        tableModel = new DefaultTableModel(columns, 0);
        JTable announcementTable = new JTable(tableModel);
        announcementTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        announcementTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(announcementTable);

        panel.add(scrollPane, BorderLayout.CENTER);

        // 初始显示所有公告
        searchAnnouncements();

        return panel;
    }

    private JTextField createStyledTextField(String tooltip) {
        JTextField field = new JTextField();
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(150, 150, 150)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        field.setToolTipText(tooltip);
        return field;
    }

    private void handlePublish(ActionEvent e) {
        String title = titleField.getText().trim();
        String content = contentArea.getText().trim();
        String date = dateField.getText().trim();

        if (title.isEmpty() || content.isEmpty() || date.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请填写完整信息", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean success = announcementDAO.addAnnouncement(title, content, date);
        if (success) {
            JOptionPane.showMessageDialog(this, "公告发布成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
            titleField.setText("");
            contentArea.setText("");
            dateField.setText(LocalDate.now().toString());
        } else {
            JOptionPane.showMessageDialog(this, "公告发布失败！", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchAnnouncements() {
        String keyword = keywordField.getText().trim();
        String date = searchDateField.getText().trim();
        List<String[]> results = announcementDAO.searchAnnouncements(keyword, date);
        tableModel.setRowCount(0);
        for (String[] row : results) {
            tableModel.addRow(row);
        }
    }

    private void switchLogin() {
        // 切换回登录界面
        new LoginFrame().setVisible(true);
        dispose();
    }
}
