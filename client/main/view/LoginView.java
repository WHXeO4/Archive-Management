package main.view;

import main.config.RoleConfiguration;
import main.controller.LoginController;
import main.entity.Response;
import main.entity.User;
import main.util.ThreadLocalUtil;
import main.view.userView.AdminView;
import main.view.userView.BrowserView;
import main.view.userView.OperatorView;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JButton loginButton;

    public LoginView() {
        setTitle("登录");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("用户名:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("密码:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        loginButton = new JButton("登录");
        panel.add(new JLabel()); // Placeholder for layout
        panel.add(loginButton);

        add(panel);

        loginButton.addActionListener(e->{
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword()); // Correct way to get password

            LoginController loginController = new LoginController();
            Response response;
            try {
                response = loginController.apply(username, password);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            if (response.isSuccess()) {
                SwingUtilities.invokeLater(() -> {
                    User user = ThreadLocalUtil.get();
                    String role = user.getRole();

                    switch (role) {
                        case RoleConfiguration.ADMINISTRATOR_ROLE:
                            try {
                                new AdminView().setVisible(true);
                            } catch (InterruptedException ex) {
                                throw new RuntimeException(ex);
                            }
                            break;
                        case RoleConfiguration.BROWSER_ROLE:
                            try {
                                new BrowserView().setVisible(true);
                            } catch (InterruptedException ex) {
                                throw new RuntimeException(ex);
                            }
                            break;
                        case RoleConfiguration.OPERATOR_ROLE:
                            try {
                                new OperatorView().setVisible(true);
                            } catch (InterruptedException ex) {
                                throw new RuntimeException(ex);
                            }
                            break;
                        default:
                            JOptionPane.showMessageDialog(null, "登录成功，但角色未知: " + role, "警告", JOptionPane.WARNING_MESSAGE);
                            break;
                    }
                    dispose(); // Close login window on success
                });
            }
        });
    }
}
