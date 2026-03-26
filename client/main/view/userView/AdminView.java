package main.view.userView;

import main.controller.*;
import main.view.managementView.DocumentListView;
import main.view.managementView.PersonalInfoManagementView;
import main.view.managementView.UserManagementView;

import javax.swing.*;
import java.awt.*;

public class AdminView extends JFrame {
    private final static String[] DOCUMENT_COLUMNS = {"标题", "描述", "更新时间", "下载"};
    private final static String ROLE = "administrator";

    public AdminView() throws InterruptedException {
        setTitle("管理员界面");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setupMenuBar();

        showUserListPanel();
    }
    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // User Management Menu
        JMenu userManagementMenu = new JMenu("用户管理");
        JMenuItem userListMenuItem = new JMenuItem("用户列表");
        userListMenuItem.addActionListener(e -> {
            try {
                showUserListPanel();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });
        userManagementMenu.add(userListMenuItem);

        // Archive Management Menu
        JMenu archiveManagementMenu = new JMenu("档案管理");
        JMenuItem docListMenuItem = new JMenuItem("档案列表");
        docListMenuItem.addActionListener(e -> {
            try {
                showDocumentListPanel();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });
        archiveManagementMenu.add(docListMenuItem);

        // Personal Info Menu
        JMenu personalInfoMenu = new JMenu("个人信息管理");
        JMenuItem changePasswordMenuItem = new JMenuItem("个人信息");
        changePasswordMenuItem.addActionListener(e -> showPasswordChangePanel());
        personalInfoMenu.add(changePasswordMenuItem);

        menuBar.add(userManagementMenu);
        menuBar.add(archiveManagementMenu);
        menuBar.add(personalInfoMenu);
        setJMenuBar(menuBar);
    }

    private void showUserListPanel() throws InterruptedException {
        UserManagementView userManagementView = new UserManagementView(this);

        userManagementView.showUserListPanel();
        setContentPane(userManagementView);
        revalidate();
        repaint();
    }

    private void showDocumentListPanel() throws InterruptedException {
        DocumentListView documentListView = new DocumentListView(this, DOCUMENT_COLUMNS, ROLE);

        documentListView.showDocumentListPanel();
        setContentPane(documentListView);
        revalidate();
        repaint();
    }

    private void showPasswordChangePanel() {
        JPanel passwordChangePanel = new PersonalInfoManagementView();
        setContentPane(passwordChangePanel);
        revalidate();
        repaint();
    }
}
