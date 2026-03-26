package main.view.managementView;

import main.controller.DocumentDownloadController;
import main.controller.DocumentListController;
import main.controller.DocumentUpdateController;
import main.controller.DocumentUploadController;
import main.entity.Document;
import main.entity.DocumentPage;
import main.entity.Response;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DocumentListView extends JPanel {
    private final static String CURRENT_VIEW = "documents";
    private final static String OPERATOR_ROLE = "operator";
    private String[] columnNames;
    private final JFrame mainFrame;
    private DefaultTableModel tableModel;
    private JTable contentTable;
    private List<Document> currentDataList;
    private int currentPage = 1;
    private final int pageSize = 10;
    private final String role;
    private JPanel documentPanel;
    private JPanel paginationPanel;
    private JTextField titleSearchField;

    public DocumentListView(JFrame mainFrame, String[] columnNames, String role) throws InterruptedException {
        this.mainFrame = mainFrame;
        this.columnNames = columnNames;
        this.role = role;

        // Set the layout for this panel to BorderLayout
        setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Make button columns editable
                return column >= columnNames.length - 2;
            }
        };
        contentTable = new JTable(tableModel);

        add(new JScrollPane(contentTable), BorderLayout.CENTER);

        init();
    }

    private void init() throws InterruptedException {
        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("标题:"));
        titleSearchField = new JTextField(15);
        searchPanel.add(titleSearchField);

        JButton searchButton = new JButton("搜索");
        searchButton.addActionListener(e -> {
            this.currentPage = 1; // Reset to first page on new search
            try {
                loadDocumentData();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });
        searchPanel.add(searchButton);

        JButton resetButton = new JButton("重置");
        resetButton.addActionListener(e -> {
            titleSearchField.setText("");
            this.currentPage = 1;
            try {
                loadDocumentData();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });
        searchPanel.add(resetButton);

        // Top Panel to hold search and document panels
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(searchPanel, BorderLayout.NORTH);

        documentPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        if (OPERATOR_ROLE.equals(role)) {
            JButton uploadButton = new JButton("上传");
            uploadButton.addActionListener(e -> {
                showUploadDialog();
            });
            documentPanel.add(uploadButton);
        }
        topPanel.add(documentPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        // Initialize pagination panel and add it to the SOUTH
        paginationPanel = new JPanel();
        add(paginationPanel, BorderLayout.SOUTH);
    }

    private void initOperator() throws InterruptedException {
        new ButtonColumn(contentTable, 3, "下载", e -> {
            downloadAction();
        });

        new ButtonColumn(contentTable, 4, "修改", e -> {
            editAction();
        });
    }

    private void initOther() throws InterruptedException {
        new ButtonColumn(contentTable, 3, "下载", e->{
            downloadAction();
        });
    }

    private void downloadAction() {
        int modelRow = contentTable.convertRowIndexToModel(contentTable.getEditingRow());
        Document cur = currentDataList.get(modelRow);

        JFileChooser fileChooser = new JFileChooser(System.getProperty("user.home"));
        fileChooser.setDialogTitle("选择下载位置");
        fileChooser.setSelectedFile(new java.io.File(cur.getTitle()+".download"));

        int userSelection = fileChooser.showSaveDialog(contentTable);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File fileToSave = fileChooser.getSelectedFile();
            String destinationPath = fileToSave.getAbsolutePath();

            DocumentDownloadController documentDownloadController = new DocumentDownloadController();
            try {
                documentDownloadController.apply(cur.getId(), destinationPath.substring(0, destinationPath.lastIndexOf("\\")));
                new File(destinationPath).deleteOnExit();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private void editAction() {
        int modelRow = contentTable.convertRowIndexToModel(contentTable.getEditingRow());
        Document cur = currentDataList.get(modelRow);

        showUpdateDialog(cur);
    }

    public void showDocumentListPanel() throws InterruptedException {
        if (OPERATOR_ROLE.equals(role)) {
            initOperator();
        } else {
            initOther();
        }

        // Clear and rebuild pagination panel
        paginationPanel.removeAll();
        JButton prevButton = new JButton("上一页");
        JButton nextButton = new JButton("下一页");
        JLabel pageLabel = new JLabel("第 " + currentPage + " 页");

        paginationPanel.add(prevButton);
        paginationPanel.add(pageLabel);
        paginationPanel.add(nextButton);

        prevButton.addActionListener(e -> {
            if (currentPage > 1) {
                currentPage--;
                try {
                    loadDocumentData();
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                pageLabel.setText("第 " + currentPage + " 页");
            }
        });

        nextButton.addActionListener(e -> {
            currentPage++;
            try {
                loadDocumentData();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            pageLabel.setText("第 " + currentPage + " 页");
        });

//        documentPanel.add(paginationPanel, BorderLayout.AFTER_LAST_LINE);

        loadDocumentData();
    }

    private void loadDocumentData() throws InterruptedException {
        String title = titleSearchField.getText();
        LocalDateTime updateTime = null;

        DocumentListController documentListController = new DocumentListController();

        Response response = documentListController.apply(title, currentPage, pageSize);
        DocumentPage documentPage = (DocumentPage) response.getData();
        if (documentPage == null || documentPage.getDocuments() == null || documentPage.getDocuments().isEmpty()) {
            if(currentPage > 1){
                JOptionPane.showMessageDialog(null,"已经到底了");
                currentPage--;
            } else {
                JOptionPane.showMessageDialog(null,"没有找到匹配的档案");
            }
            // Clear table if no results on the first page
            if (currentPage == 1) {
                updateDocumentTable();
            }
            return;
        }

        currentDataList = documentPage.getDocuments();

        updateDocumentTable();
    }

    private void updateDocumentTable() {
        tableModel.setRowCount(0);
        if (currentDataList != null) {
            for (Document doc : currentDataList) {
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                if (OPERATOR_ROLE.equals(role)) {
                    tableModel.addRow(new Object[]{doc.getTitle(), doc.getDescription(), doc.getUpdateTime().format(dateTimeFormatter), "", ""});
                } else {
                    tableModel.addRow(new Object[]{doc.getTitle(), doc.getDescription(), doc.getUpdateTime().format(dateTimeFormatter), ""});
                }
            }
        }
    }

    private void showUploadDialog() {
        JDialog uploadDialog = new JDialog(mainFrame, "文件上传", true);
        uploadDialog.setSize(450, 250);
        uploadDialog.setLocationRelativeTo(mainFrame);
        uploadDialog.setLayout(new BorderLayout());

        // Input Panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("标题:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        JTextField titleField = new JTextField(20);
        inputPanel.add(titleField, gbc);

        // Description
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        inputPanel.add(new JLabel("描述:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        JTextArea descriptionArea = new JTextArea(3, 20);
        inputPanel.add(new JScrollPane(descriptionArea), gbc);

        // File Path
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        inputPanel.add(new JLabel("文件地址:"), gbc);
        gbc.gridx = 1;
        JTextField filePathField = new JTextField(20);
        filePathField.setEditable(false);
        inputPanel.add(filePathField, gbc);

        gbc.gridx = 2;
        JButton browseButton = new JButton("选择文件");
        inputPanel.add(browseButton, gbc);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton okButton = new JButton("确定");
        JButton cancelButton = new JButton("取消");
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        // Add panels to dialog
        uploadDialog.add(inputPanel, BorderLayout.CENTER);
        uploadDialog.add(buttonPanel, BorderLayout.SOUTH);

        // Action Listeners
        browseButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(uploadDialog);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                filePathField.setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });

        okButton.addActionListener(e -> {
            String title = titleField.getText();
            String description = descriptionArea.getText();
            String filePath = filePathField.getText();

            if (title.isEmpty() || filePath.isEmpty()) {
                JOptionPane.showMessageDialog(uploadDialog, "标题和文件地址不能为空！", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // --- Interface call placeholder ---
            // Assuming DocumentUploadController has a constructor that takes these parameters
            DocumentUploadController documentUploadController = new DocumentUploadController();
            try {
                documentUploadController.apply(title, description, filePath);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
//            JOptionPane.showMessageDialog(uploadDialog, "已触发上传逻辑！\n标题: " + title + "\n描述: " + description + "\n路径: " + filePath);

            uploadDialog.dispose();
            try {
                loadDocumentData(); // Refresh the list after upload
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });

        cancelButton.addActionListener(e -> uploadDialog.dispose());

        uploadDialog.setVisible(true);
    }

    private void showUpdateDialog(Document doc) {
        JDialog updateDialog = new JDialog(mainFrame, "修改档案信息", true);
        updateDialog.setSize(450, 250);
        updateDialog.setLocationRelativeTo(mainFrame);
        updateDialog.setLayout(new BorderLayout());

        // Input Panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("标题:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        JTextField titleField = new JTextField(20);
        titleField.setText(doc.getTitle());
        inputPanel.add(titleField, gbc);

        // Description
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        inputPanel.add(new JLabel("描述:"), gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        JTextArea descriptionArea = new JTextArea(3, 20);
        descriptionArea.setText(doc.getDescription());
        inputPanel.add(new JScrollPane(descriptionArea), gbc);

        // File Path (for replacement)
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        inputPanel.add(new JLabel("替换文件:"), gbc);
        gbc.gridx = 1;
        JTextField filePathField = new JTextField(20);
        filePathField.setEditable(false);
        inputPanel.add(filePathField, gbc);

        gbc.gridx = 2;
        JButton browseButton = new JButton("选择文件");
        inputPanel.add(browseButton, gbc);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton okButton = new JButton("确定");
        JButton cancelButton = new JButton("取消");
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        // Add panels to dialog
        updateDialog.add(inputPanel, BorderLayout.CENTER);
        updateDialog.add(buttonPanel, BorderLayout.SOUTH);

        // Action Listeners
        browseButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(updateDialog);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                filePathField.setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });

        okButton.addActionListener(e -> {
            String title = titleField.getText();
            String description = descriptionArea.getText();
            String newFilePath = filePathField.getText(); // Can be empty if not replacing

            if (title.isEmpty()) {
                JOptionPane.showMessageDialog(updateDialog, "标题不能为空！", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (newFilePath.isEmpty()) {
                newFilePath = null;
            }

            DocumentUpdateController documentUpdateController = new DocumentUpdateController();
            try {
                documentUpdateController.apply(doc.getId(), title, description, newFilePath);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }

            updateDialog.dispose();
            try {
                loadDocumentData(); // Refresh the list after update
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });

        cancelButton.addActionListener(e -> updateDialog.dispose());

        updateDialog.setVisible(true);
    }
}
