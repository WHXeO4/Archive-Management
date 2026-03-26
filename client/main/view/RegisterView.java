package main.view;

import main.controller.RegisterController;

import javax.swing.*;
import java.awt.*;

public class RegisterView extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private ButtonGroup roleGroup;
    private JRadioButton adminRadioButton, browserRadioButton, operatorRadioButton;
    private JButton registerButton;

    public RegisterView() {
        setTitle("用户注册");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 输入面板
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.add(new JLabel("用户名:"));
        usernameField = new JTextField();
        inputPanel.add(usernameField);

        inputPanel.add(new JLabel("密码:"));
        passwordField = new JPasswordField();
        inputPanel.add(passwordField);

        // 角色选择面板
        JPanel rolePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rolePanel.add(new JLabel("角色:"));
        adminRadioButton = new JRadioButton("Administrator");
        adminRadioButton.setActionCommand("Administrator");
        browserRadioButton = new JRadioButton("Browser");
        browserRadioButton.setActionCommand("Browser");
        operatorRadioButton = new JRadioButton("Operator");
        operatorRadioButton.setActionCommand("Operator");

        roleGroup = new ButtonGroup();
        roleGroup.add(adminRadioButton);
        roleGroup.add(browserRadioButton);
        roleGroup.add(operatorRadioButton);
        browserRadioButton.setSelected(true); // 默认选中Browser

        rolePanel.add(adminRadioButton);
        rolePanel.add(browserRadioButton);
        rolePanel.add(operatorRadioButton);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        registerButton = new JButton("注册");
        buttonPanel.add(registerButton);

        // 组合面板
        JPanel centerPanel = new JPanel(new BorderLayout(10,10));
        centerPanel.add(inputPanel, BorderLayout.CENTER);
        centerPanel.add(rolePanel, BorderLayout.SOUTH);


        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // 添加事件监听器
        registerButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String role = roleGroup.getSelection().getActionCommand();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "用户名和密码不能为空！", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            RegisterController registerController = new RegisterController();
            try {
                registerController.apply(username, password, role);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }

            dispose(); // 关闭注册窗口
        });
    }
}
