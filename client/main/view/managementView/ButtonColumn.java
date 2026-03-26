package main.view.managementView;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionListener;

class ButtonColumn extends AbstractCellEditor implements TableCellRenderer, TableCellEditor {
    private final JTable table;
    private final JButton renderButton;
    private final JButton editButton;
    private final String text;

    public ButtonColumn(JTable table, int column, String text, ActionListener listener) {
        this.table = table;
        this.text = text;
        this.renderButton = new JButton(text);
        this.editButton = new JButton(text);
        editButton.addActionListener(listener);
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(column).setCellRenderer(this);
        columnModel.getColumn(column).setCellEditor(this);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return renderButton;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        return editButton;
    }

    @Override
    public Object getCellEditorValue() {
        return text;
    }
}