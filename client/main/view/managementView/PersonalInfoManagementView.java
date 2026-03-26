package main.view.managementView;

import main.controller.UpdatePasswordController;
import main.entity.Response;
import main.entity.User;
import main.util.ThreadLocalUtil;

import javax.swing.*;
import java.awt.*;

public class PersonalInfoManagementView extends JPanel {

    public PersonalInfoManagementView() {
        setLayout(new BorderLayout());

        // Fetch current user info
        User currentUser = ThreadLocalUtil.get();

        // Panel to display user info
        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel usernameLabel = new JLabel("用户名: " + (currentUser != null ? currentUser.getName() : "N/A"));
        JLabel roleLabel = new JLabel("身份: " + (currentUser != null ? currentUser.getRole() : "N/A"));
        JButton changePasswordButton = new JButton("修改密码");
        JButton logoutButton = new JButton("退出");

        infoPanel.add(usernameLabel);
        infoPanel.add(roleLabel);
        infoPanel.add(changePasswordButton);
        infoPanel.add(logoutButton);

        add(infoPanel, BorderLayout.NORTH);

        // Action listener for the change password button
        changePasswordButton.addActionListener(e -> {
            showChangePasswordDialog();
        });

        logoutButton.addActionListener(e -> {
            ThreadLocalUtil.set(null);
            SwingUtilities.getWindowAncestor(this).dispose();
        });
    }

    private void showChangePasswordDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "修改密码", true);
        dialog.setSize(350, 200);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("旧密码:"), gbc);

        gbc.gridx = 1;
        JPasswordField oldPasswordField = new JPasswordField(20);
        panel.add(oldPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("新密码:"), gbc);

        gbc.gridx = 1;
        JPasswordField newPasswordField = new JPasswordField(20);
        panel.add(newPasswordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("确认新密码:"), gbc);

        gbc.gridx = 1;
        JPasswordField confirmPasswordField = new JPasswordField(20);
        panel.add(confirmPasswordField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton okButton = new JButton("确定");
        JButton cancelButton = new JButton("取消");
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        okButton.addActionListener(e -> {
            String oldPassword = new String(oldPasswordField.getPassword());
            String newPassword = new String(newPasswordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "所有字段都不能为空！", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(dialog, "新密码和确认密码不匹配！", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            UpdatePasswordController updatePasswordController = new UpdatePasswordController();
            try {
                Response response = updatePasswordController.apply(oldPassword, newPassword);
                if (response != null && response.isSuccess()) {
                    // Close the OperatorView window
                    SwingUtilities.getWindowAncestor(this).dispose();
                    dialog.dispose();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }
}


