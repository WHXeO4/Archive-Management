package main.view;

import javax.swing.*;
import java.awt.*;

public class WelcomeView extends JFrame {

    public WelcomeView() {
        // 设置窗口标题
        setTitle("文档管理系统");
        // 设置窗口大小
        setSize(400, 300);
        // 设置关闭操作
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 窗口居中
        setLocationRelativeTo(null);

        // 创建主面板
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 欢迎标签
        JLabel welcomeLabel = new JLabel("欢迎使用文档管理系统", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("宋体", Font.BOLD, 24));
        mainPanel.add(welcomeLabel, BorderLayout.NORTH);

        // 按钮面板
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1, 10, 10));

        // 创建按钮
        JButton registerButton = new JButton("注册");
        JButton loginButton = new JButton("登录");
        JButton quitButton = new JButton("退出");

        // 添加按钮到面板
        buttonPanel.add(registerButton);
        buttonPanel.add(loginButton);
        buttonPanel.add(quitButton);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        // 添加主面板到窗口
        add(mainPanel);

        // 添加事件监听器
        registerButton.addActionListener(e -> {
            // 打开注册窗口
            RegisterView registerView = new RegisterView();
            registerView.setVisible(true);
        });

        loginButton.addActionListener(e -> {
            // 打开登录窗口
            LoginView loginView = new LoginView();
            loginView.setVisible(true);
        });

        quitButton.addActionListener(e -> {
            // 退出应用
            System.exit(0);
        });
    }
}

