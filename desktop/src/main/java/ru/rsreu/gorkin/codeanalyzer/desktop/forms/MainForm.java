package ru.rsreu.gorkin.codeanalyzer.desktop.forms;

import javax.swing.*;
import java.awt.*;
import java.util.function.Supplier;

public class MainForm {
    private JTabbedPane tabbedPane;
    private JPanel parentPanel;

    public MainForm() {
        initWindow();
    }

    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    public void setTabbedPane(JTabbedPane tabbedPane) {
        this.tabbedPane = tabbedPane;
    }

    public JPanel getParentPanel() {
        return parentPanel;
    }

    public void setParentPanel(JPanel parentPanel) {
        this.parentPanel = parentPanel;
    }

    public void addCloseableTab(String title, Component component) {
        tabbedPane.addTab(title, component);
        int index = tabbedPane.indexOfTab(title);
        JPanel pnlTab = new JPanel(new GridBagLayout());
        pnlTab.setOpaque(false);
        JLabel lblTitle = new JLabel(title);
        JButton btnClose = new JButton("x");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;

        pnlTab.add(lblTitle, gbc);

        gbc.gridx++;
        gbc.weightx = 0;
        pnlTab.add(btnClose, gbc);

        tabbedPane.setTabComponentAt(index, pnlTab);

        btnClose.addActionListener(evt -> {
            int listnerIndex = tabbedPane.indexOfTab(title);
            if (listnerIndex >= 0) {
                tabbedPane.removeTabAt(index);
            }
        });

    }

    public void addNoneCloseableTab(String title, Component component) {
        tabbedPane.addTab(title, component);
    }

    private void initWindow() {
        tabbedPane.removeAll();
    }
}
