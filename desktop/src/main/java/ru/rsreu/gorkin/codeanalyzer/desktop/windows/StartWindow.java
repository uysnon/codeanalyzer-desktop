package ru.rsreu.gorkin.codeanalyzer.desktop.windows;

import ru.rsreu.gorkin.codeanalyzer.core.adapters.TreeCreator;
import ru.rsreu.gorkin.codeanalyzer.core.metrics.Metric;
import ru.rsreu.gorkin.codeanalyzer.core.syntaxelements.SourceCodeUnit;
import ru.rsreu.gorkin.codeanalyzer.desktop.App;
import ru.rsreu.gorkin.codeanalyzer.desktop.elements.files.JavaFilesChooser;
import ru.rsreu.gorkin.codeanalyzer.desktop.elements.files.fileutils.ExtensionUtils;
import ru.rsreu.gorkin.codeanalyzer.desktop.elements.files.fileutils.FilesFinder;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class StartWindow {
    private static final String WINDOW_CAPTION = "Main page";
    private static final String CHOOSE_FILE_MESSAGE = "Выбрать файл";
    private static final int WINDOW_HEIGHT = 540;
    private static final int WINDOW_WIDTH = 900;

    private List<File> selectedFilesAndDirectories;
    private Map<File, SourceCodeUnit> fileSourceCodeUnitMap;
    private TreeCreator treeCreator;

    private JPanel panel;
    private JFrame windowJFrame;
    private JButton chooseFileButton;
    private JTable javaFilesTable;
    private JScrollPane scrollPaneForJavaFilesTable;
    private DefaultTableModel javaFilesTableModel;
    private JFileChooser fileChooser;

    private Color backgroundColorForBaseElements;

    public StartWindow() {
        initDataStructures();
        initWindow();
    }

    private void initDataStructures() {
        backgroundColorForBaseElements = new Color(240, 239, 235);
        selectedFilesAndDirectories = new ArrayList<>();
        fileSourceCodeUnitMap = new HashMap<>();
        treeCreator = new TreeCreator();
    }

    private void initWindow() {
        windowJFrame = new JFrame(WINDOW_CAPTION);
        windowJFrame.getContentPane().setBackground(backgroundColorForBaseElements);
        windowJFrame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        //TODO
//        ImageIcon img = new ImageIcon("java.png");
//        windowJFrame.setIconImage(img.getImage());


        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(backgroundColorForBaseElements);

        fileChooser = new JavaFilesChooser();

        chooseFileButton = new JButton(CHOOSE_FILE_MESSAGE);
        chooseFileButton.addActionListener(e -> {
            int returnVal = fileChooser.showOpenDialog(windowJFrame);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                try {
                    List<File> javaFiles = FilesFinder.findFilesFromFilesAndDirectories(fileChooser.getSelectedFiles());
                    updateSourceCodeUnitsMap(javaFiles);
                    updateTable();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
        chooseFileButton.setBackground(backgroundColorForBaseElements);

        javaFilesTableModel = new DefaultTableModel();
        javaFilesTableModel.addColumn("Файл");
        javaFilesTableModel.addColumn("Метрики");

        javaFilesTable = new JTable(javaFilesTableModel);
        javaFilesTable.setBackground(backgroundColorForBaseElements);


        scrollPaneForJavaFilesTable = new JScrollPane(javaFilesTable);
        scrollPaneForJavaFilesTable.setBackground(backgroundColorForBaseElements);

        panel.add(chooseFileButton);
        panel.add(scrollPaneForJavaFilesTable);


        windowJFrame.add(panel);
        windowJFrame.setLocationRelativeTo(null);
        windowJFrame.setVisible(true);
    }

    private void updateSourceCodeUnitsMap(List<File> newFiles) throws FileNotFoundException {
        fileSourceCodeUnitMap.clear();
        for (File file : newFiles) {
            fileSourceCodeUnitMap.put(file, treeCreator.createSourceCodeUnit(file));
        }
    }

    private void updateTable() {
        javaFilesTableModel.setRowCount(0);
        fileSourceCodeUnitMap.keySet().stream()
                .sorted(Comparator.comparing(file -> ExtensionUtils.getFileNameFrom(file.toString())))
                .forEach(file -> {
                    Metric metric = fileSourceCodeUnitMap.get(file).getMetrics().get(0);
                    javaFilesTableModel.addRow(new Object[]
                            {
                                    ExtensionUtils.getFileNameFrom(file.toString()),
                                    String.format("%s - %s", metric.getTitle(), metric.getCount())
                            });
                });
    }

}
