package ru.rsreu.gorkin.codeanalyzer.desktop.ui.forms;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import ru.rsreu.gorkin.codeanalyzer.desktop.excel.export.ExcelExporter;
import ru.rsreu.gorkin.codeanalyzer.desktop.screenshots.Screenshotmaker;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        parentPanel = new JPanel();
        parentPanel.setLayout(new BorderLayout(0, 0));
        final JSplitPane splitPane1 = new JSplitPane();
        splitPane1.setDividerLocation(360);
        splitPane1.setDoubleBuffered(true);
        parentPanel.add(splitPane1, BorderLayout.CENTER);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        splitPane1.setLeftComponent(panel1);
        exportDiagramButton = new JButton();
        exportDiagramButton.setText("Экспорт диаграммы");
        panel1.add(exportDiagramButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        diagramScrollPane = new JScrollPane();
        panel1.add(diagramScrollPane, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        diagramParentPanel = new JPanel();
        diagramParentPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        diagramScrollPane.setViewportView(diagramParentPanel);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        splitPane1.setRightComponent(panel2);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel2.add(panel3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        unitLabel = new JLabel();
        unitLabel.setText("Label");
        panel3.add(unitLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new BorderLayout(0, 0));
        panel2.add(panel4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        sourceCodePanel = new JTabbedPane();
        panel4.add(sourceCodePanel, BorderLayout.CENTER);
        metricsPanel = new JPanel();
        metricsPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        sourceCodePanel.addTab("Метрики", metricsPanel);
        exportMetricsButton = new JButton();
        exportMetricsButton.setText("Экспорт метрик");
        metricsPanel.add(exportMetricsButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new BorderLayout(0, 0));
        metricsPanel.add(panel5, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel5.add(scrollPane1, BorderLayout.CENTER);
        metricsTable = new JTable();
        scrollPane1.setViewportView(metricsTable);
        programTextPanel = new JPanel();
        programTextPanel.setLayout(new BorderLayout(0, 0));
        sourceCodePanel.addTab("Программный код", programTextPanel);
        final JScrollPane scrollPane2 = new JScrollPane();
        programTextPanel.add(scrollPane2, BorderLayout.CENTER);
        unitTextArea = new JTextArea();
        Font unitTextAreaFont = this.$$$getFont$$$("Courier New", Font.PLAIN, 12, unitTextArea.getFont());
        if (unitTextAreaFont != null) unitTextArea.setFont(unitTextAreaFont);
        scrollPane2.setViewportView(unitTextArea);
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return parentPanel;
    }
}
