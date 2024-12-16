package release;

import release.ui.LoginFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // 设置界面风格，可选择系统默认风格提升美观度
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}
