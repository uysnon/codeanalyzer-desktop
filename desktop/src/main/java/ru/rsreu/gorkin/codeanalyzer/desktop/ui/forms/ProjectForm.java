package ru.rsreu.gorkin.codeanalyzer.desktop.ui.forms;

import ru.rsreu.gorkin.codeanalyzer.core.adapters.TreeCreator;
import ru.rsreu.gorkin.codeanalyzer.core.metrics.Metric;
import ru.rsreu.gorkin.codeanalyzer.core.syntaxelements.SourceCodeUnit;
import ru.rsreu.gorkin.codeanalyzer.desktop.metrics.SourceFileMetrics;
import ru.rsreu.gorkin.codeanalyzer.desktop.ui.elements.files.JavaFilesChooser;
import ru.rsreu.gorkin.codeanalyzer.desktop.ui.elements.files.fileutils.ExtensionUtils;
import ru.rsreu.gorkin.codeanalyzer.desktop.ui.elements.files.fileutils.FilesFinder;
import ru.rsreu.gorkin.codeanalyzer.desktop.ui.elements.table.NoneEditableTableModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ProjectForm {
    private JButton selectProgramProjectButton;
    private JTable projectMetricsTable;
    private JPanel parentPanel;

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
                    tableRow.add(  ExtensionUtils.getFileNameFrom(file.toString()));
                    metrics.forEach(metric->tableRow.add(String.valueOf(metric.getCount())));
                    projectMetricsTableModel.addRow(tableRow.toArray());
                });

        int a = 1;
    }


    private void createUIComponents() {
        initDataStructures();
        initFormElements();
    }
}
