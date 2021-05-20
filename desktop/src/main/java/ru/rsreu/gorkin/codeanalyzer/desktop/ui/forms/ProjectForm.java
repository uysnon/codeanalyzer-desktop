package ru.rsreu.gorkin.codeanalyzer.desktop.ui.forms;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import org.apache.commons.io.FilenameUtils;
import ru.rsreu.gorkin.codeanalyzer.core.adapters.TreeCreator;
import ru.rsreu.gorkin.codeanalyzer.core.metrics.Metric;
import ru.rsreu.gorkin.codeanalyzer.core.syntaxelements.SourceCodeUnit;
import ru.rsreu.gorkin.codeanalyzer.desktop.excel.export.ExcelExporter;
import ru.rsreu.gorkin.codeanalyzer.desktop.metrics.SourceFileMetrics;
import ru.rsreu.gorkin.codeanalyzer.desktop.ui.elements.files.JavaFilesChooser;
import ru.rsreu.gorkin.codeanalyzer.desktop.ui.elements.files.fileutils.ExtensionUtils;
import ru.rsreu.gorkin.codeanalyzer.desktop.ui.elements.files.fileutils.FilesFinder;
import ru.rsreu.gorkin.codeanalyzer.desktop.ui.elements.table.NoneEditableTableModel;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ProjectForm {
    private JButton selectProgramProjectButton;
    private JTable projectMetricsTable;
    private JPanel parentPanel;
    private JButton exportMetricsButton;

    private List<File> selectedFilesAndDirectories;

    private JScrollPane scrollPaneForJavaFilesTable;
    private DefaultTableModel projectMetricsTableModel;
    private JFileChooser javaFileChooser;

    private TreeCreator treeCreator;

    private Map<File, SourceCodeUnit> fileSourceCodeUnitMap;
    private Consumer<Map<File, SourceCodeUnit>> projectStructureConsumer;
    private Consumer<SourceCodeUnit> clickSourceCodeUnitConsumer;

    public ProjectForm() {
        fileSourceCodeUnitMap = new HashMap<>();
        initDataStructures();
        initFormElements();
        exportMetricsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportMetrics();
            }
        });
    }


    public void setClickSourceCodeUnitConsumer(Consumer<SourceCodeUnit> clickSourceCodeUnitConsumer) {
        this.clickSourceCodeUnitConsumer = clickSourceCodeUnitConsumer;
    }

    public void setProjectStructureConsumer(Consumer<Map<File, SourceCodeUnit>> projectStructureConsumer) {
        this.projectStructureConsumer = projectStructureConsumer;
    }

    public JPanel getParentPanel() {
        return parentPanel;
    }

    public List<SourceCodeUnit> getSourceCodeUnits() {
        return new ArrayList<>(fileSourceCodeUnitMap.values());
    }

    private void initDataStructures() {
        selectedFilesAndDirectories = new ArrayList<>();
        fileSourceCodeUnitMap = new HashMap<>();
        treeCreator = new TreeCreator();
    }

    private void initFormElements() {
        initJavaFilesChooser();
        initSelectProjectButton();
        initMetricsTable();
    }

    private void initMetricsTable() {
        projectMetricsTableModel = new NoneEditableTableModel();
        projectMetricsTableModel.addColumn("Файл");
        for (String shortMetricsName : Arrays.stream(SourceFileMetrics.values()).map(SourceFileMetrics::getShortName).sorted(String::compareTo).collect(Collectors.toList())) {
            projectMetricsTableModel.addColumn(shortMetricsName);
        }
        projectMetricsTable.setModel(projectMetricsTableModel);
        projectMetricsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable target = (JTable) e.getSource();
                    int row = target.getSelectedRow();
                    clickSourceCodeUnitConsumer.accept(
                            fileSourceCodeUnitMap.get(selectedFilesAndDirectories.get(row))
                    );
                }
            }
        });
    }

    private void initJavaFilesChooser() {
        this.javaFileChooser = new JavaFilesChooser();
    }

    private void initSelectProjectButton() {
        selectProgramProjectButton.addActionListener(e -> {
            int returnVal = javaFileChooser.showOpenDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                try {
                    List<File> javaFiles = FilesFinder.findFilesFromFilesAndDirectories(javaFileChooser.getSelectedFiles());
                    updateSourceCodeUnitsMap(javaFiles);
                    updateTable();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
    }

    private void updateSourceCodeUnitsMap(List<File> newFiles) throws FileNotFoundException {
        fileSourceCodeUnitMap.clear();
        for (File file : newFiles) {
            SourceCodeUnit unit = treeCreator.createSourceCodeUnit(file);
            unit.setFileName(ExtensionUtils.getFileNameFrom(file.getPath()));
            fileSourceCodeUnitMap.put(file, unit);
        }
    }

    private void updateTable() {
        projectMetricsTableModel.setRowCount(0);
        selectedFilesAndDirectories = new ArrayList<>();
        fileSourceCodeUnitMap.keySet().stream()
                .sorted(Comparator.comparing(file -> ExtensionUtils.getFileNameFrom(file.toString())))
                .forEach(file -> {
                    selectedFilesAndDirectories.add(file);
                    List<Metric> metrics = fileSourceCodeUnitMap
                            .get(file)
                            .getMetrics()
                            .stream()
                            .sorted(Comparator.comparing(SourceFileMetrics::getShortName))
                            .collect(Collectors.toList());
                    List<String> tableRow = new ArrayList<>();
                    tableRow.add(ExtensionUtils.getFileNameFrom(file.toString()));
                    metrics.forEach(metric -> tableRow.add(String.valueOf(metric.getCount())));
                    projectMetricsTableModel.addRow(tableRow.toArray());
                });

        int a = 1;
    }

    private void createUIComponents() {
        initDataStructures();
        initFormElements();
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
                        getFileNamesWithMetrics(),
                        new File(fileChooser.getSelectedFile().getAbsoluteFile().toString() + ".xls"));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private Map<String, List<Metric>> getFileNamesWithMetrics() {
        Map<String, List<Metric>> fileNamesWithMetrics = new HashMap<>();
        Set<File> files = fileSourceCodeUnitMap.keySet();
        for (File file : files) {
            String fileName = FilenameUtils.getName(file.getAbsolutePath().toString());
            SourceCodeUnit sourceCodeUnit = fileSourceCodeUnitMap.get(file);
            List<Metric> metrics = sourceCodeUnit.getMetrics();
            fileNamesWithMetrics.put(fileName, metrics);
        }
        return fileNamesWithMetrics;
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
        parentPanel.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        parentPanel.setBackground(new Color(-524289));
        parentPanel.putClientProperty("html.disable", Boolean.FALSE);
        selectProgramProjectButton = new JButton();
        selectProgramProjectButton.setText("Выбрать программный проект");
        parentPanel.add(selectProgramProjectButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        parentPanel.add(scrollPane1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        projectMetricsTable = new JTable();
        projectMetricsTable.setBackground(new Color(-1));
        Font projectMetricsTableFont = this.$$$getFont$$$("Dubai", Font.BOLD, 12, projectMetricsTable.getFont());
        if (projectMetricsTableFont != null) projectMetricsTable.setFont(projectMetricsTableFont);
        scrollPane1.setViewportView(projectMetricsTable);
        exportMetricsButton = new JButton();
        exportMetricsButton.setText("Экспорт метрик");
        parentPanel.add(exportMetricsButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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
