package com.example.ui;

import com.example.dao.DeviceDAO;
import javax.swing.*;
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

    // 新增：通过角色决定权限
    public DeviceFrame(String role) {
        this.role = role;
        deviceDAO = new DeviceDAO();
        setTitle("设备管理");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(mainPanel);

        // 顶部搜索区域
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.add(new JLabel("关键字："));
        keywordField = new JTextField(10);
        topPanel.add(keywordField);

        topPanel.add(new JLabel("类型过滤："));
        typeFilterField = new JTextField(10);
        topPanel.add(typeFilterField);

        JButton searchButton = new JButton("搜索");
        searchButton.addActionListener(e -> searchDevices());
        topPanel.add(searchButton);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // 中间表格区域
        String[] columns = { "ID", "名称", "类型", "状态" };
        tableModel = new DefaultTableModel(columns, 0);
        deviceTable = new JTable(tableModel);
        deviceTable.setAutoCreateRowSorter(true);
        JScrollPane scrollPane = new JScrollPane(deviceTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // 底部操作区域：新增、修改、删除
        JPanel bottomPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        bottomPanel.add(new JLabel("名称："), gbc);
        nameField = new JTextField(10);
        gbc.gridx = 1;
        bottomPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        bottomPanel.add(new JLabel("类型："), gbc);
        typeField = new JTextField(10);
        gbc.gridx = 1;
        bottomPanel.add(typeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        bottomPanel.add(new JLabel("状态："), gbc);
        statusField = new JTextField(10);
        gbc.gridx = 1;
        bottomPanel.add(statusField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        addButton = new JButton("添加");
        addButton.addActionListener(e -> addDevice());
        updateButton = new JButton("修改");
        updateButton.addActionListener(e -> updateDevice());
        deleteButton = new JButton("删除");
        deleteButton.addActionListener(e -> deleteDevice());
        JButton chartButton = new JButton("状态统计图表");
        chartButton.addActionListener(e -> openChart());

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(chartButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        bottomPanel.add(buttonPanel, gbc);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // 初始显示所有设备
        searchDevices();

        // 根据角色决定权限
        if (!"admin".equalsIgnoreCase(role) && !"管理员".equalsIgnoreCase(role)) {
            // 非管理员角色（用户）禁用添加、修改、删除功能
            addButton.setEnabled(false);
            updateButton.setEnabled(false);
            deleteButton.setEnabled(false);
        }
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
}
