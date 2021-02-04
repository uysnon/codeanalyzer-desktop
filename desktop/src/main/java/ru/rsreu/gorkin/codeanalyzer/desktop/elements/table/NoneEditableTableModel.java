package ru.rsreu.gorkin.codeanalyzer.desktop.elements.table;

import javax.swing.table.DefaultTableModel;

public class NoneEditableTableModel extends DefaultTableModel {
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
