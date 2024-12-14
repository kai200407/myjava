package com.example.ui;

import com.example.dao.UserDAO;
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private UserDAO userDAO;

    public LoginFrame() {
        userDAO = new UserDAO();
        setTitle("校园网络设备管理系统 - 登录");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // ignore
        }

        BackgroundPanel mainPanel = new BackgroundPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(mainPanel);

        // 顶部面板（标题）
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("校园网络设备管理系统", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setForeground(new Color(50, 50, 50));
        topPanel.add(titleLabel, BorderLayout.NORTH);

        JLabel infoLabel = new JLabel(
                "<html><div style='text-align: center;'>网络安全为人民，网络安全靠人民<br>遵守法律法规，共建和谐校园环境</div></html>",
                SwingConstants.CENTER);
        infoLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        infoLabel.setForeground(new Color(80, 80, 80));
        topPanel.add(infoLabel, BorderLayout.CENTER);

        mainPanel.add(topPanel, BorderLayout.NORTH);

        // 中间表单区
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        mainPanel.add(formPanel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 10, 15, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel userLabel = new JLabel("用户名:");
        userLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        userLabel.setForeground(new Color(60, 60, 60));
        usernameField = new JTextField(15);
        usernameField.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JLabel passLabel = new JLabel("密 码:");
        passLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        passLabel.setForeground(new Color(60, 60, 60));
        passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("SansSerif", Font.PLAIN, 14));

        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(userLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(passLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        // 底部按钮区
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);

        RoundedButton loginButton = new RoundedButton("登 录");
        RoundedButton registerButton = new RoundedButton("注 册");
        registerButton.setBackground(new Color(34, 167, 240));

        loginButton.addActionListener(e -> handleLogin());
        registerButton.addActionListener(e -> openRegisterFrame());

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void handleLogin() {

        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        UserDAO userDAO = new UserDAO();
        boolean valid = userDAO.validateLogin(username, password);
        if (valid) {
            // 获取角色
            String role = userDAO.getUserRole(username);
            System.out.println("用户角色：" + role); // 调试用，确保角色不为 null

            if ("admin".equalsIgnoreCase(role)) {
                // 管理员进入公告发布页面
                new AnnouncementFrame().setVisible(true);
            } else {
                // 普通用户进入公告查看页面
                new AnnouncementViewFrame().setVisible(true);
            }
            System.out.println("登录成功，用户名：" + username + "，角色：" + role);
        } else {
            JOptionPane.showMessageDialog(this, "用户名或密码错误！", "错误", JOptionPane.ERROR_MESSAGE);
        }

        // 关闭当前登录窗口
        this.dispose();

    }

    private void openRegisterFrame() {
        SwingUtilities.invokeLater(() -> new RegisterFrame().setVisible(true));
    }
}
