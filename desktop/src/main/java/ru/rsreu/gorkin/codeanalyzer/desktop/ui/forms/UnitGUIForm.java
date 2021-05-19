package ru.rsreu.gorkin.codeanalyzer.desktop.ui.forms;

import ru.rsreu.gorkin.codeanalyzer.desktop.excel.export.ExcelExporter;
import ru.rsreu.gorkin.codeanalyzer.desktop.screenshots.Screenshotmaker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Paths;
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
        exportDiagramButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                makeScreenshotAction();
            }
        });
        exportMetricsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportMetrics();
            }
        });
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

    private void makeScreenshotAction() {
        JFileChooser fileChooser = new JFileChooser();
        String currentPath = Paths.get("").toAbsolutePath().toString();
        fileChooser.setCurrentDirectory(new File(currentPath));
        int retrival = fileChooser.showSaveDialog(null);
        if (retrival == JFileChooser.APPROVE_OPTION) {
            try {
                Screenshotmaker screenshotMaker = new Screenshotmaker();
                screenshotMaker.makeScreenshot(
                        diagramPanel,
                        new File(fileChooser.getSelectedFile().getAbsoluteFile().toString() + ".png"));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void exportMetrics() {
        JFileChooser fileChooser = new JFileChooser();
        String currentPath = Paths.get("").toAbsolutePath().toString();
        fileChooser.setCurrentDirectory(new File(currentPath));
        int retrival = fileChooser.showSaveDialog(null);
        if (retrival == JFileChooser.APPROVE_OPTION) {
            try {
                ExcelExporter excelExporter = new ExcelExporter();
                excelExporter.export(
                        getCurrentUnit().getMetrics(),
                        new File(fileChooser.getSelectedFile().getAbsoluteFile().toString() + ".xls"));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
