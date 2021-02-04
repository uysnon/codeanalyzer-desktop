package ru.rsreu.gorkin.codeanalyzer.desktop.frames;

import ru.rsreu.gorkin.codeanalyzer.core.syntaxelements.ClassOrInterfaceUnit;
import ru.rsreu.gorkin.codeanalyzer.core.syntaxelements.EnumUnit;
import ru.rsreu.gorkin.codeanalyzer.core.syntaxelements.SourceCodeUnit;
import ru.rsreu.gorkin.codeanalyzer.desktop.elements.panels.FileDiagramPanel;
import ru.rsreu.gorkin.codeanalyzer.desktop.elements.table.NoneEditableTableModel;
import ru.rsreu.gorkin.codeanalyzer.desktop.elements.utils.MetricsBySpecificityComparator;
import ru.rsreu.gorkin.codeanalyzer.desktop.elements.utils.TextColorUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class JavaFileDialog {
    private static final Color classColor = new Color(25, 114, 120);
    private static final Color interfaceColor = new Color(196, 69, 54);
    private static final Color enumColor = new Color(119, 46, 37);

    private static final String CLASS_TEXT_COLOR = "EDDDD4";

    private static final String ENUM_TITLE = "enum";
    private static final String CLASS_TITLE = "class";
    private static final String INTERFACE_TITLE = "interface";

    private String fileName;
    private SourceCodeUnit sourceCodeUnit;

    private Frame parentFrame;

    private JDialog dialog;
    private JPanel panel;
    private JPanel schemaPanel;
    private JScrollPane scrollPaneForDiagram;
    private JLabel label;

    private JTable metricsTable;
    private DefaultTableModel metricsTableModel;
    private JScrollPane scrollPaneForJavaMetricsTable;

    private List<JButton> classes;


    public JavaFileDialog(String fileName, SourceCodeUnit sourceCodeUnit, Frame owner) {
        this.fileName = fileName;
        this.sourceCodeUnit = sourceCodeUnit;
        this.parentFrame = owner;
        this.dialog = new JDialog(owner);
        initDialogElements();
    }

    public void show() {
        dialog.setVisible(true);
    }

    private void initDialogElements() {
        classes = new ArrayList<>();

        panel = new JPanel(new FlowLayout());
        panel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        label = new JLabel(fileName);

        metricsTableModel = new NoneEditableTableModel();
        metricsTableModel.addColumn("Метрика");
        metricsTableModel.addColumn("Величина");
        sourceCodeUnit.getMetrics().stream()
                .sorted(new MetricsBySpecificityComparator())
                .forEach(metric ->
                metricsTableModel.addRow(new Object[]
                        {
                                metric.getTitle(),
                                metric.getCount()
                        })
        );

        metricsTable = new JTable(metricsTableModel);

        scrollPaneForJavaMetricsTable = new JScrollPane(metricsTable);

        for (ClassOrInterfaceUnit unit : sourceCodeUnit.getClassOrInterfaceUnits()) {
            JButton button = null;
            String name = unit.getClassOrInterfaceDeclaration().getNameAsString();
            if (unit.getClassOrInterfaceDeclaration().isInterface()) {
                button = new JButton(
                        TextColorUtils.getColorStringWithSupText(
                                name,
                                CLASS_TEXT_COLOR,
                                INTERFACE_TITLE,
                                CLASS_TEXT_COLOR));
                button.setBackground(interfaceColor);

            } else {
                button = new JButton(
                        TextColorUtils.getColorStringWithSupText(
                                name,
                                CLASS_TEXT_COLOR,
                                CLASS_TITLE,
                                CLASS_TEXT_COLOR));
                button.setBackground(classColor);
            }
            button.addMouseListener(
                    new MouseAdapter() {

                        @Override
                        public void mousePressed(MouseEvent e) {
                            if (e.getClickCount() == 2) {
                                ClassDialog classDialog = new ClassDialog(
                                        name,
                                        unit,
                                        parentFrame);
                                classDialog.show();
                            }
                        }
                    }
            );
            button.setBorderPainted(false);
            classes.add(button);
        }

        for (EnumUnit unit : sourceCodeUnit.getEnumUnits()) {
            JButton button = new JButton(
                    TextColorUtils.getColorStringWithSupText(
                            unit.getEnumDeclaration().getNameAsString(),
                            CLASS_TEXT_COLOR,
                            ENUM_TITLE,
                            CLASS_TEXT_COLOR));
            button.setBackground(enumColor);
            button.setBorderPainted(false);
            classes.add(button);
        }

        int cols = classes.size() >= 2 ? 2 : 1;
        GridLayout gridLayout = new GridLayout(0, cols, 40, 40);

        schemaPanel = new FileDiagramPanel(classes, gridLayout);
        schemaPanel.setBackground(Color.WHITE);

        classes.forEach(schemaPanel::add);

        scrollPaneForDiagram = new JScrollPane(schemaPanel);
        scrollPaneForDiagram.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPaneForDiagram.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        panel.add(label);
        panel.add(scrollPaneForDiagram);
        panel.add(scrollPaneForJavaMetricsTable);


        dialog.setSize(1200, 500);
        dialog.add(panel);
        dialog.setTitle(fileName);
    }

}
