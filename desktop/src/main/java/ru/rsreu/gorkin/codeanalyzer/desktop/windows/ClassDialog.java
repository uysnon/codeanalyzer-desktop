package ru.rsreu.gorkin.codeanalyzer.desktop.windows;

import ru.rsreu.gorkin.codeanalyzer.core.syntaxelements.ClassOrInterfaceUnit;
import ru.rsreu.gorkin.codeanalyzer.core.syntaxelements.EnumUnit;
import ru.rsreu.gorkin.codeanalyzer.core.syntaxelements.MethodUnit;
import ru.rsreu.gorkin.codeanalyzer.desktop.elements.panels.ClassDiagramPanel;
import ru.rsreu.gorkin.codeanalyzer.desktop.elements.utils.TextColorUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ClassDialog {
    private static final Color FIELD_COLOR = new Color(25, 114, 120);
    private static final Color METHOD_COLOR = new Color(196, 69, 54);
    private static final Color CONSTRUCTOR_COLOR = new Color(119, 46, 37);

    private static final String CLASS_TEXT_COLOR = "EDDDD4";

    private static final String FIELD_TITLE = "field";
    private static final String METHOD_TITLE = "method";
    private static final String CONSTRUCTOR_TITLE = "constructor";

    private String className;
    private ClassOrInterfaceUnit classUnit;

    private JDialog dialog;
    private JPanel panel;
    private JPanel schemaPanel;
    private JLabel label;

    private JTable metricsTable;
    private DefaultTableModel metricsTableModel;
    private JScrollPane scrollPaneForJavaMetricsTable;

    private List<JButton> classElementsButtons;


    public ClassDialog(String className, ClassOrInterfaceUnit classUnit, Frame owner) {
        this.className = className;
        this.classUnit = classUnit;
        this.dialog = new JDialog(owner);
        initDialogElements();
    }

    public void show() {
        dialog.setVisible(true);
    }

    private void initDialogElements() {
        classElementsButtons = new ArrayList<>();

        panel = new JPanel(new FlowLayout());
        panel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        label = new JLabel(className);

        metricsTableModel = new DefaultTableModel();
        metricsTableModel.addColumn("Метрика");
        metricsTableModel.addColumn("Величина");
        classUnit.getMetrics().forEach(metric ->
                metricsTableModel.addRow(new Object[]
                        {
                                metric.getTitle(),
                                metric.getCount()
                        })
        );

        metricsTable = new JTable(metricsTableModel);

        scrollPaneForJavaMetricsTable = new JScrollPane(metricsTable);

        for (MethodUnit unit : classUnit.getMethodUnits()) {
            JButton button = null;
            button = new JButton(
                    TextColorUtils.getColorStringWithSupText(
                            unit.getMethodDeclaration().getDeclarationAsString(true,true, true),
                            CLASS_TEXT_COLOR,
                            METHOD_TITLE,
                            CLASS_TEXT_COLOR));
            button.setBackground(METHOD_COLOR);
            button.setBorderPainted(false);
            classElementsButtons.add(button);
        }

//        int cols = classElementsButtons.size() >= 2 ? 2 : 1;
        int cols = 1;
        GridLayout gridLayout = new GridLayout(0, cols, 10, 5);

        schemaPanel = new ClassDiagramPanel(classElementsButtons, gridLayout);
        schemaPanel.setBackground(Color.WHITE);

        classElementsButtons.forEach(schemaPanel::add);
        panel.add(label);
        panel.add(schemaPanel);
        panel.add(scrollPaneForJavaMetricsTable);


        dialog.setSize(1200, 500);
        dialog.add(panel);
        dialog.setTitle(className);
    }

}
