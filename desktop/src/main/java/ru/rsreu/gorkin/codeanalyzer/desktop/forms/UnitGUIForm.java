package ru.rsreu.gorkin.codeanalyzer.desktop.forms;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class UnitGUIForm extends UnitForm {
    public static final Color FILE_COLOR = new Color(25, 114, 120);
    public static final Color CLASS_COLOR = new Color(196, 69, 54);
    public static final Color INTERFACE_COLOR = new Color(119, 46, 37);
    public static final Color ENUM_COLOR = new Color(119, 46, 37);
    public static final String HEX_LIGHT_COLOR = "EDDDD4";


    protected JPanel parentPanel;
    protected JTabbedPane sourceCodePanel;
    protected JButton exportDiagramButton;
    protected JPanel diagramPanel;
    protected JPanel metricsPanel;
    protected JPanel programTextPanel;
    protected JTable metricsTable;
    protected JTextArea unitTextArea;
    protected JButton exportMetricsButton;
    protected JLabel unitLabel;
    protected JScrollPane diagramScrollPane;
    protected JPanel diagramParentPanel;

    protected List<JButton> units;


    public UnitGUIForm() {
        this.units = new ArrayList<>();
    }


    public JPanel getParentPanel() {
        return parentPanel;
    }

    @Override
    protected JTextArea getUnitTextArea() {
        return unitTextArea;
    }

    @Override
    protected JTable getMetricsTable() {
        return metricsTable;
    }

    @Override
    protected JLabel getUnitLabel() {
        return unitLabel;
    }

    protected abstract void createUIComponents();

    protected abstract void initDiagramPanel();


}
