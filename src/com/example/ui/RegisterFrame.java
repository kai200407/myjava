package com.example.ui;

import com.example.dao.UserDAO;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField roleField;
    private UserDAO userDAO;

    public RegisterFrame() {
        userDAO = new UserDAO();
        setTitle("校园网络设备管理系统 - 注册");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null);
        setResizable(false);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // ignore
        }

        BackgroundPanel mainPanel = new BackgroundPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        add(mainPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 10, 15, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel titleLabel = new JLabel("用户注册", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setForeground(new Color(50, 50, 50));

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(titleLabel, gbc);

        gbc.anchor = GridBagConstraints.WEST; 
        gbc.gridwidth = 1;

        JLabel userLabel = new JLabel("用户名：");
        userLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        userLabel.setForeground(new Color(60, 60, 60));

        usernameField = new JTextField(15);
        usernameField.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JLabel passLabel = new JLabel("密 码：");
        passLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        passLabel.setForeground(new Color(60, 60, 60));

        passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JLabel roleLabel = new JLabel("角 色：");
        roleLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        roleLabel.setForeground(new Color(60, 60, 60));

        roleField = new JTextField(15);
        roleField.setFont(new Font("SansSerif", Font.PLAIN, 14));

        gbc.gridx = 0; gbc.gridy = 1; mainPanel.add(userLabel, gbc);
        gbc.gridx = 1; mainPanel.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; mainPanel.add(passLabel, gbc);
        gbc.gridx = 1; mainPanel.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; mainPanel.add(roleLabel, gbc);
        gbc.gridx = 1; mainPanel.add(roleField, gbc);

        RoundedButton registerButton = new RoundedButton("注 册");
        registerButton.setBackground(new Color(46, 204, 113));
        registerButton.addActionListener(e -> handleRegister());

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(registerButton, gbc);
    }

    private void handleRegister() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String role = roleField.getText().trim();

        if (username.isEmpty() || password.isEmpty() || role.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请填写完整信息", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean success = userDAO.registerUser(username, password, role);
        if (success) {
            JOptionPane.showMessageDialog(this, "注册成功，请返回登录界面！", "成功", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "注册失败，可能用户名已存在", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
}
