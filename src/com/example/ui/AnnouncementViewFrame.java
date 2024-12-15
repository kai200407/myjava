package com.example.ui;

import com.example.dao.AnnouncementDAO;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AnnouncementViewFrame extends JFrame {
    private JTextField keywordField;
    private JTextField dateField;
    private DefaultTableModel tableModel;
    private AnnouncementDAO announcementDAO;

    public AnnouncementViewFrame() {
        announcementDAO = new AnnouncementDAO();
        setTitle("公告查看（用户）");
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

        JLabel titleLabel = new JLabel("公告查看", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(new Color(50, 50, 50));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.add(titleLabel, BorderLayout.CENTER);

        // 右侧按钮区域
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        rightPanel.setOpaque(false);

        // 添加“切换登录”按钮
        RoundedButton switchUserButton = new RoundedButton("切换登录");
        switchUserButton.setPreferredSize(new Dimension(100, 30));
        switchUserButton.addActionListener(e -> switchLogin());
        rightPanel.add(switchUserButton);

        // 添加“设备管理”按钮
        RoundedButton deviceManageButton = new RoundedButton("设备管理");
        deviceManageButton.setPreferredSize(new Dimension(100, 30));
        deviceManageButton.addActionListener(e -> {
            // 打开设备管理界面，并根据用户身份加载对应的数据
            new DeviceFrame("user").setVisible(true); // 假设当前角色是普通用户
        });
        rightPanel.add(deviceManageButton);

        topPanel.add(rightPanel, BorderLayout.EAST);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // 中间区域（搜索和公告列表）
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setOpaque(false); // 设置透明，主背景颜色透过
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // 搜索栏
        JPanel topSearchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topSearchPanel.setOpaque(false); // 透明背景
        topSearchPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(150, 150, 150)),
                "搜索公告",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 16),
                new Color(60, 60, 60)));

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

        // 公告列表
        String[] columns = { "标题", "内容", "发布日期" };
        tableModel = new DefaultTableModel(columns, 0);
        JTable announcementTable = new JTable(tableModel);

        // 设置公告列表样式
        announcementTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        announcementTable.setRowHeight(25);
        announcementTable.setBackground(new Color(0, 0, 0, 0)); // 设置背景透明
        announcementTable.setOpaque(false); // 表格透明背景

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

        centerPanel.add(scrollPane, BorderLayout.CENTER);

        // 初始化显示所有公告
        searchAnnouncements();
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
