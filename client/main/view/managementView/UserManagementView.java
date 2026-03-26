package main.view.managementView;

import main.controller.*;
import main.entity.Document;
import main.entity.Response;
import main.entity.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.List;

public class UserManagementView extends JPanel {
    private final static String[] columnNames = {"用户名", "密码", "身份", "", ""};
    private DefaultTableModel tableModel;
    private JTable contentTable;
    private List<User> currentDataList;
    private final JFrame mainFrame;

    public UserManagementView(JFrame mainFrame) {
        this.mainFrame = mainFrame;

        setLayout(new BorderLayout());

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton addUserButton = new JButton("新增用户");
        addUserButton.addActionListener(e -> showAddUserDialog());
        userPanel.add(addUserButton);

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Make button columns editable
                return column >= columnNames.length - 2;
            }
        };
        contentTable = new JTable(tableModel);

        add(userPanel, BorderLayout.NORTH);
        add(new JScrollPane(contentTable), BorderLayout.CENTER);
    }

    public void showUserListPanel() throws InterruptedException {
        new ButtonColumn(contentTable, 3, "修改", e->{
            editAction();
        });
        new ButtonColumn(contentTable, 4, "删除", e->{
            deleteAction();
        });
        loadUserTable();
    }

    private void editAction() {
        int modelRow = contentTable.convertRowIndexToModel(contentTable.getEditingRow());
        User user = currentDataList.get(modelRow);

        showEditUserDialog(user);
    }

    private void deleteAction() {
        int modelRow = contentTable.convertRowIndexToModel(contentTable.getEditingRow());
        User user = currentDataList.get(modelRow);
        int confirmation = JOptionPane.showConfirmDialog(contentTable, "确定要删除用户 " + user.getName() + " 吗?", "确认删除", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            UserDeleteController deleteController = new UserDeleteController();
            try {
                Response response = deleteController.apply(user.getId());
                if(response.isSuccess()){
                    loadUserTable();
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void loadUserTable() throws InterruptedException {
        UserListController userListController = new UserListController();
        Response response = userListController.apply();
        if (response != null && response.isSuccess()) {
            currentDataList = (List<User>) response.getData();
            updateUserTable();
        }
    }

    private void updateUserTable() {
        tableModel.setRowCount(0);
        if (currentDataList != null) {
            for (User user : currentDataList) {
                tableModel.addRow(new Object[]{user.getName(), user.getPassword(), user.getRole(), "修改", "删除"});
            }
        }
    }

    private void showAddUserDialog() {
        JDialog addUserDialog = new JDialog(mainFrame, "新增用户", true);
        addUserDialog.setSize(400, 300);
        addUserDialog.setLocationRelativeTo(mainFrame);
        addUserDialog.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        inputPanel.add(new JLabel("用户名:"));
        JTextField usernameField = new JTextField();
        inputPanel.add(usernameField);

        inputPanel.add(new JLabel("密码:"));
        JPasswordField passwordField = new JPasswordField();
        inputPanel.add(passwordField);

        // Role Panel
        JPanel rolePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rolePanel.add(new JLabel("角色:"));
        JRadioButton adminRadioButton = new JRadioButton("Administrator");
        adminRadioButton.setActionCommand("Administrator");
        JRadioButton browserRadioButton = new JRadioButton("Browser");
        browserRadioButton.setActionCommand("Browser");
        JRadioButton operatorRadioButton = new JRadioButton("Operator");
        operatorRadioButton.setActionCommand("Operator");

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(adminRadioButton);
        buttonGroup.add(browserRadioButton);
        buttonGroup.add(operatorRadioButton);
        browserRadioButton.setSelected(true); // Default selection

        rolePanel.add(adminRadioButton);
        rolePanel.add(browserRadioButton);
        rolePanel.add(operatorRadioButton);

        // Center Panel to combine input and role
        JPanel centerPanel = new JPanel(new BorderLayout(10,10));
        centerPanel.add(inputPanel, BorderLayout.CENTER);
        centerPanel.add(rolePanel, BorderLayout.SOUTH);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton okButton = new JButton("确定");
        JButton cancelButton = new JButton("取消");
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        addUserDialog.add(mainPanel);

        okButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String role = buttonGroup.getSelection().getActionCommand();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(addUserDialog, "用户名和密码不能为空！", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            RegisterController registerController = new RegisterController();
            try {
                Response response = registerController.apply(username, password, role);
                if (response.isSuccess()) {
                    addUserDialog.dispose();
                    loadUserTable(); // Refresh user list
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        });

        cancelButton.addActionListener(e -> addUserDialog.dispose());
        addUserDialog.setVisible(true);
    }

    private void showEditUserDialog(User user) {
        JDialog editUserDialog = new JDialog(mainFrame, "修改用户", true);
        editUserDialog.setSize(400, 300);
        editUserDialog.setLocationRelativeTo(mainFrame);
        editUserDialog.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        inputPanel.add(new JLabel("用户名:"));
        JTextField usernameField = new JTextField(user.getName());
        inputPanel.add(usernameField);

        inputPanel.add(new JLabel("新密码 (留空则不修改):"));
        JPasswordField passwordField = new JPasswordField();
        inputPanel.add(passwordField);

        // Role Panel
        JPanel rolePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rolePanel.add(new JLabel("角色:"));
        JRadioButton adminRadioButton = new JRadioButton("Administrator");
        adminRadioButton.setActionCommand("Administrator");
        JRadioButton browserRadioButton = new JRadioButton("Browser");
        browserRadioButton.setActionCommand("Browser");
        JRadioButton operatorRadioButton = new JRadioButton("Operator");
        operatorRadioButton.setActionCommand("Operator");

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(adminRadioButton);
        buttonGroup.add(browserRadioButton);
        buttonGroup.add(operatorRadioButton);

        // Set the default selection based on the user's current role
        switch (user.getRole()) {
            case "Administrator":
                adminRadioButton.setSelected(true);
                break;
            case "Browser":
                browserRadioButton.setSelected(true);
                break;
            case "Operator":
                operatorRadioButton.setSelected(true);
                break;
        }

        rolePanel.add(adminRadioButton);
        rolePanel.add(browserRadioButton);
        rolePanel.add(operatorRadioButton);

        // Center Panel to combine input and role
        JPanel centerPanel = new JPanel(new BorderLayout(10,10));
        centerPanel.add(inputPanel, BorderLayout.CENTER);
        centerPanel.add(rolePanel, BorderLayout.SOUTH);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton okButton = new JButton("确定");
        JButton cancelButton = new JButton("取消");
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        editUserDialog.add(mainPanel);

        okButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (password.isEmpty()) {
                password = user.getPassword();
            }
            String role = buttonGroup.getSelection().getActionCommand();

            UserEditController userEditController = new UserEditController();
            try {
                Response response = userEditController.apply(user.getId(), username, password, role);
                if (response.isSuccess()) {
                    editUserDialog.dispose();
                    loadUserTable(); // Refresh user list
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        });

        cancelButton.addActionListener(e -> editUserDialog.dispose());
        editUserDialog.setVisible(true);
    }
}
