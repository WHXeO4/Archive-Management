package main.view.userView;

import main.view.managementView.DocumentListView;
import main.view.managementView.PersonalInfoManagementView;

import javax.swing.*;
import java.awt.*;

public class OperatorView extends JFrame {
    private final static String[] DOCUMENT_COLUMNS = {"标题", "描述", "更新时间", "下载", "修改"};
    private final static String ROLE = "operator";

    public OperatorView() throws InterruptedException {
        setTitle("档案录入员界面");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu archiveManagementMenu = new JMenu("档案管理");
        JMenu personalInfoManagementMenu = new JMenu("个人信息管理");

        JMenuItem listMenuItem = new JMenuItem("档案列表");
        archiveManagementMenu.add(listMenuItem);

        JMenuItem viewInfoMenuItem = new JMenuItem("查看信息");
        personalInfoManagementMenu.add(viewInfoMenuItem);

        listMenuItem.addActionListener(e -> {
            try {
                showDocumentList();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });

        viewInfoMenuItem.addActionListener(e -> showUserInfoPanel());

        menuBar.add(archiveManagementMenu);
        menuBar.add(personalInfoManagementMenu);

        showDocumentList();
    }

    private void showDocumentList() throws InterruptedException {
        DocumentListView documentListView = new DocumentListView(this, DOCUMENT_COLUMNS, ROLE);
        documentListView.showDocumentListPanel();

        setContentPane(documentListView);
        revalidate();
        repaint();
    }

    private void showUserInfoPanel() {
        PersonalInfoManagementView personalInfoManagementView = new PersonalInfoManagementView();
        setContentPane(personalInfoManagementView);
        revalidate();
        repaint();
    }
}
