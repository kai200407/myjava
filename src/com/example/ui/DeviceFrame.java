package com.example.ui;

import com.example.dao.DeviceDAO;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DeviceFrame extends JFrame {
    private DeviceDAO deviceDAO;
    private JTextField nameField, typeField, statusField;
    private JTextField keywordField, typeFilterField;
    private DefaultTableModel tableModel;
    private JTable deviceTable;
    private JButton addButton, updateButton, deleteButton;
    private String role;

    public DeviceFrame(String role) {
        this.role = role;
        deviceDAO = new DeviceDAO();
        setTitle("设备管理");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // 主背景面板（渐变背景）
        BackgroundPanel mainPanel = new BackgroundPanel();
        mainPanel.setLayout(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(mainPanel);

        // 顶部区域：标题 + 按钮
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("设备管理", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(new Color(50, 50, 50));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.add(titleLabel, BorderLayout.CENTER);

        // 右侧按钮区
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        rightPanel.setOpaque(false);

        // 切换登录按钮
        RoundedButton switchUserButton = new RoundedButton("切换登录");
        switchUserButton.setPreferredSize(new Dimension(100, 30));
        switchUserButton.addActionListener(e -> switchLogin());
        rightPanel.add(switchUserButton);

        // 状态统计图表按钮
        RoundedButton chartButton = new RoundedButton("状态统计图表");
        chartButton.setPreferredSize(new Dimension(120, 30));
        chartButton.addActionListener(e -> openChart());
        rightPanel.add(chartButton);

        topPanel.add(rightPanel, BorderLayout.EAST);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // 中间区域：搜索 + 列表
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setOpaque(false);

        // 搜索区
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setOpaque(false);

        TitledBorder searchBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(150, 150, 150)),
                "搜索设备",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 14),
                new Color(60, 60, 60));
        searchPanel.setBorder(searchBorder);

        searchPanel.add(new JLabel("关键字："));
        keywordField = createStyledTextField("输入名称、类型或状态关键字");
        searchPanel.add(keywordField);

        searchPanel.add(new JLabel("类型过滤："));
        typeFilterField = createStyledTextField("如：服务器、路由器");
        searchPanel.add(typeFilterField);

        RoundedButton searchButton = new RoundedButton("搜索");
        searchButton.setPreferredSize(new Dimension(80, 30));
        searchButton.addActionListener(e -> searchDevices());
        searchPanel.add(searchButton);

        centerPanel.add(searchPanel, BorderLayout.NORTH);

        // 设备列表区域
        String[] columns = { "ID", "名称", "类型", "状态" };
        tableModel = new DefaultTableModel(columns, 0);
        deviceTable = new JTable(tableModel);
        deviceTable.setAutoCreateRowSorter(true);
        deviceTable.setFont(new Font("SansSerif", Font.PLAIN, 14));
        deviceTable.setRowHeight(25);

        // 设置设备列表透明，显示主页面颜色
        deviceTable.setOpaque(false);
        deviceTable.setBackground(new Color(0, 0, 0, 0));

        // 表头和单元格渲染器透明化处理
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setOpaque(false);
        deviceTable.setDefaultRenderer(Object.class, cellRenderer);

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setFont(new Font("SansSerif", Font.BOLD, 14));
        headerRenderer.setForeground(new Color(50, 50, 50));
        headerRenderer.setOpaque(false);
        deviceTable.getTableHeader().setDefaultRenderer(headerRenderer);

        JScrollPane scrollPane = new JScrollPane(deviceTable);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        TitledBorder listBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(150, 150, 150)),
                "设备列表",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 14),
                new Color(60, 60, 60));
        scrollPane.setBorder(listBorder);

        centerPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // 底部编辑区（管理员可用）
        JPanel bottomPanel = new JPanel(new GridBagLayout());
        bottomPanel.setOpaque(false);

        TitledBorder editBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(150, 150, 150)),
                "添加/修改/删除设备（管理员）",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 14),
                new Color(60, 60, 60));
        bottomPanel.setBorder(editBorder);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // 名称
        gbc.gridx = 0;
        gbc.gridy = 0;
        bottomPanel.add(new JLabel("名称："), gbc);
        nameField = createStyledTextField("如：主服务器");
        nameField.setColumns(15);
        gbc.gridx = 1;
        bottomPanel.add(nameField, gbc);

        // 类型
        gbc.gridx = 0;
        gbc.gridy = 1;
        bottomPanel.add(new JLabel("类型："), gbc);
        typeField = createStyledTextField("如：服务器、交换机");
        typeField.setColumns(15);
        gbc.gridx = 1;
        bottomPanel.add(typeField, gbc);

        // 状态
        gbc.gridx = 0;
        gbc.gridy = 2;
        bottomPanel.add(new JLabel("状态："), gbc);
        statusField = createStyledTextField("如：正常、故障");
        statusField.setColumns(15);
        gbc.gridx = 1;
        bottomPanel.add(statusField, gbc);

        // 按钮区
        JPanel opButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        opButtonPanel.setOpaque(false);

        addButton = new RoundedButton("添加");
        addButton.addActionListener(e -> addDevice());

        updateButton = new RoundedButton("修改");
        updateButton.addActionListener(e -> updateDevice());

        deleteButton = new RoundedButton("删除");
        deleteButton.addActionListener(e -> deleteDevice());

        opButtonPanel.add(addButton);
        opButtonPanel.add(updateButton);
        opButtonPanel.add(deleteButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        bottomPanel.add(opButtonPanel, gbc);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // 初始显示所有设备
        searchDevices();

        // 根据角色决定权限
        if (!"admin".equalsIgnoreCase(role) && !"管理员".equalsIgnoreCase(role)) {
            addButton.setEnabled(false);
            updateButton.setEnabled(false);
            deleteButton.setEnabled(false);
            editBorder.setTitle("设备信息（仅查看）");
            bottomPanel.setBorder(editBorder);
        }
    }

    private JTextField createStyledTextField(String tooltip) {
        JTextField field = new JTextField(15);
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        // 增加背景和圆角边框让其更美观
        field.setBackground(new Color(255, 255, 255, 220)); // 半透明白色背景
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(150, 150, 150), 1, true),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        field.setToolTipText(tooltip);
        field.setOpaque(true); // 确保背景可见
        return field;
    }

    private void searchDevices() {
        String keyword = keywordField.getText().trim();
        String typeFilter = typeFilterField.getText().trim();
        List<String[]> results = deviceDAO.searchDevices(keyword, typeFilter);
        tableModel.setRowCount(0);
        for (String[] row : results) {
            tableModel.addRow(row);
        }
    }

    private void addDevice() {
        String name = nameField.getText().trim();
        String type = typeField.getText().trim();
        String status = statusField.getText().trim();
        if (name.isEmpty() || type.isEmpty() || status.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请填写完整信息", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        boolean success = deviceDAO.addDevice(name, type, status);
        if (success) {
            JOptionPane.showMessageDialog(this, "设备添加成功", "成功", JOptionPane.INFORMATION_MESSAGE);
            searchDevices();
        } else {
            JOptionPane.showMessageDialog(this, "添加失败", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateDevice() {
        int row = deviceTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "请选择要修改的设备", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int id = Integer.parseInt((String) tableModel.getValueAt(row, 0));
        String name = nameField.getText().trim();
        String type = typeField.getText().trim();
        String status = statusField.getText().trim();
        if (name.isEmpty() || type.isEmpty() || status.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请填写完整信息", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean success = deviceDAO.updateDevice(id, name, type, status);
        if (success) {
            JOptionPane.showMessageDialog(this, "设备修改成功", "成功", JOptionPane.INFORMATION_MESSAGE);
            searchDevices();
        } else {
            JOptionPane.showMessageDialog(this, "修改失败", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteDevice() {
        int row = deviceTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "请选择要删除的设备", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int id = Integer.parseInt((String) tableModel.getValueAt(row, 0));
        int option = JOptionPane.showConfirmDialog(this, "确定删除该设备吗？", "确认", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            boolean success = deviceDAO.deleteDevice(id);
            if (success) {
                JOptionPane.showMessageDialog(this, "设备删除成功", "成功", JOptionPane.INFORMATION_MESSAGE);
                searchDevices();
            } else {
                JOptionPane.showMessageDialog(this, "删除失败", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void openChart() {
        new DeviceChartFrame().setVisible(true);
    }

    private void switchLogin() {
        new LoginFrame().setVisible(true);
        dispose();
    }
}
