package ru.rsreu.gorkin.codeanalyzer.desktop.forms;

import ru.rsreu.gorkin.codeanalyzer.core.adapters.TreeCreator;
import ru.rsreu.gorkin.codeanalyzer.core.metrics.Metric;
import ru.rsreu.gorkin.codeanalyzer.core.syntaxelements.SourceCodeUnit;
import ru.rsreu.gorkin.codeanalyzer.desktop.elements.files.JavaFilesChooser;
import ru.rsreu.gorkin.codeanalyzer.desktop.elements.files.fileutils.ExtensionUtils;
import ru.rsreu.gorkin.codeanalyzer.desktop.elements.files.fileutils.FilesFinder;
import ru.rsreu.gorkin.codeanalyzer.desktop.elements.table.NoneEditableTableModel;
import ru.rsreu.gorkin.codeanalyzer.desktop.frames.JavaFileDialog;

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
        projectMetricsTableModel.addColumn("Метрики");
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
                    Metric metric = fileSourceCodeUnitMap.get(file).getMetrics().get(0);
                    projectMetricsTableModel.addRow(new Object[]
                            {
                                    ExtensionUtils.getFileNameFrom(file.toString()),
                                    String.format("%s - %s", metric.getTitle(), metric.getCount())
                            });
                });

        int a = 1;
    }


    private void createUIComponents() {
        initDataStructures();
        initFormElements();
    }
}
