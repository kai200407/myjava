package com.example.ui;

import com.example.dao.AnnouncementDAO;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
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
        announcementDAO = new AnnouncementDAO();
        setTitle("公告管理（管理员）");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // 主背景面板（渐变背景）
        BackgroundPanel mainPanel = new BackgroundPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(mainPanel);

        // 顶部区域（标题 + 按钮）
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("公告管理中心", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(new Color(50, 50, 50));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.add(titleLabel, BorderLayout.CENTER);

        // 右侧按钮区域
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        rightPanel.setOpaque(false);

        RoundedButton switchUserButton = new RoundedButton("切换登录");
        switchUserButton.setPreferredSize(new Dimension(100, 30));
        switchUserButton.addActionListener(e -> switchLogin());
        rightPanel.add(switchUserButton);

        RoundedButton deviceManageButton = new RoundedButton("设备管理");
        deviceManageButton.setPreferredSize(new Dimension(100, 30));
        deviceManageButton.addActionListener(e -> new DeviceFrame("admin").setVisible(true));
        rightPanel.add(deviceManageButton);

        topPanel.add(rightPanel, BorderLayout.EAST);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setFont(new Font("SansSerif", Font.PLAIN, 14));

        // 发布公告面板
        BackgroundPanel publishPanel = new BackgroundPanel();
        publishPanel.setLayout(new GridBagLayout());
        publishPanel.setOpaque(false);
        createPublishPanelContent(publishPanel);
        tabbedPane.addTab("发布公告", publishPanel);

        // // 查看公告面板
        // BackgroundPanel viewPanel = new BackgroundPanel();
        // viewPanel.setLayout(new BorderLayout(10, 10));
        // viewPanel.setOpaque(false);
        // createViewPanelContent(viewPanel);
        // tabbedPane.addTab("查看公告", viewPanel);

        // mainPanel.add(tabbedPane, BorderLayout.CENTER);

        // 查看公告面板
        BackgroundPanel viewPanel = new BackgroundPanel();
        viewPanel.setLayout(new BorderLayout(10, 10));
        viewPanel.setOpaque(false);

        // 调用用户界面的查看公告方法
        JPanel userViewPanel = createUserViewPanel(); // 重用用户查看公告的逻辑
        viewPanel.add(userViewPanel, BorderLayout.CENTER);

        tabbedPane.addTab("查看公告", viewPanel);

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        // 初始化公告列表
        searchAnnouncements();
    }

    private void createPublishPanelContent(JPanel panel) {
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
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 标题
        JLabel titleLabel = new JLabel("标题：");
        titleLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        titleField = createStyledTextField("示例：服务器维护通知");
        titleField.setColumns(30);

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

        contentArea = new JTextArea(8, 30);
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
        dateField.setColumns(30);
        dateField.setText(LocalDate.now().toString());

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        panel.add(dateLabel, gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(dateField, gbc);

        // 发布按钮
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
    }

    private void createViewPanelContent(JPanel panel) {
        panel.setOpaque(false);
        TitledBorder tb = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(150, 150, 150), 1),
                "查看和搜索公告",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 16),
                new Color(60, 60, 60));
        panel.setBorder(tb);

        // 顶部搜索区域
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

        searchDateField = createStyledTextField("如：2024-12");
        searchDateField.setToolTipText("可输入部分日期（如2024-12）查找该月公告");
        topSearchPanel.add(searchDateField);

        // 添加搜索按钮（仿照用户界面）
        RoundedButton searchButton = new RoundedButton("搜索");
        searchButton.addActionListener(e -> searchAnnouncements());
        topSearchPanel.add(searchButton);

        // 将搜索面板添加到顶部
        panel.add(topSearchPanel, BorderLayout.NORTH);

        // 公告列表区域
        String[] columns = { "标题", "内容", "发布日期" };
        tableModel = new DefaultTableModel(columns, 0);
        JTable announcementTable = new JTable(tableModel);
        announcementTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        announcementTable.setRowHeight(25);

        // 设置表格透明背景
        announcementTable.setBackground(new Color(0, 0, 0, 0));
        announcementTable.setOpaque(false);

        // 设置单元格渲染器为透明
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setOpaque(false);
        announcementTable.setDefaultRenderer(Object.class, cellRenderer);

        // 设置表头透明背景
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setFont(new Font("SansSerif", Font.BOLD, 16));
        headerRenderer.setForeground(new Color(50, 50, 50));
        headerRenderer.setOpaque(false);
        announcementTable.getTableHeader().setDefaultRenderer(headerRenderer);
        announcementTable.getTableHeader().setReorderingAllowed(false); // 禁止拖动表头

        JScrollPane scrollPane = new JScrollPane(announcementTable);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(150, 150, 150)),
                "公告列表",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 16),
                new Color(60, 60, 60)));

        // 确保公告列表区域正确添加到中心
        panel.add(scrollPane, BorderLayout.CENTER);
    }

    private JTextField createStyledTextField(String tooltip) {
        JTextField field = new JTextField(15);
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
        String keyword = (keywordField == null) ? "" : keywordField.getText().trim();
        String date = (searchDateField == null) ? "" : searchDateField.getText().trim();
        List<String[]> results = announcementDAO.searchAnnouncements(keyword, date);
        if (tableModel != null) {
            tableModel.setRowCount(0);
            for (String[] row : results) {
                tableModel.addRow(row);
            }
        }
    }

    private JPanel createUserViewPanel() {
        // 创建用户查看公告的内容
        JPanel userViewPanel = new JPanel(new BorderLayout(10, 10));
        userViewPanel.setOpaque(false);

        // 搜索栏部分
        JPanel topSearchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topSearchPanel.setOpaque(false); // 透明背景

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

        userViewPanel.add(topSearchPanel, BorderLayout.NORTH);

        // 公告列表部分
        String[] columns = { "标题", "内容", "发布日期" };
        tableModel = new DefaultTableModel(columns, 0);
        JTable announcementTable = new JTable(tableModel);

        // 设置公告列表样式
        announcementTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        announcementTable.setRowHeight(25);
        announcementTable.setBackground(new Color(0, 0, 0, 0)); // 设置背景透明
        announcementTable.setOpaque(false); // 表格透明背景

        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setOpaque(false);
        announcementTable.setDefaultRenderer(Object.class, cellRenderer);

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setFont(new Font("SansSerif", Font.BOLD, 16));
        headerRenderer.setForeground(new Color(50, 50, 50));
        headerRenderer.setOpaque(false);
        announcementTable.getTableHeader().setDefaultRenderer(headerRenderer);
        announcementTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(announcementTable);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(150, 150, 150)),
                "公告列表",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 16),
                new Color(60, 60, 60)));

        userViewPanel.add(scrollPane, BorderLayout.CENTER);

        // 初始化公告列表
        searchAnnouncements();

        return userViewPanel;
    }

    private void switchLogin() {
        new LoginFrame().setVisible(true);
        dispose();
    }
}
