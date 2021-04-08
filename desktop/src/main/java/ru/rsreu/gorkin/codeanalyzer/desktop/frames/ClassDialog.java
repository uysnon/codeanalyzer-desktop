package ru.rsreu.gorkin.codeanalyzer.desktop.frames;

import com.github.javaparser.ast.Node;
import com.github.javaparser.printer.PrettyPrinter;
import ru.rsreu.gorkin.codeanalyzer.core.syntaxelements.*;
import ru.rsreu.gorkin.codeanalyzer.desktop.elements.panels.ClassDiagramPanel;
import ru.rsreu.gorkin.codeanalyzer.desktop.elements.table.NoneEditableTableModel;
import ru.rsreu.gorkin.codeanalyzer.desktop.elements.utils.MetricsBySpecificityComparator;
import ru.rsreu.gorkin.codeanalyzer.desktop.elements.utils.TextColorUtils;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;


public class ClassDialog {
    private static final int DIALOG_WIDTH = 1700;
    private static final int DIALOG_HEIGHT = 700;

    private static final int DIAGRAM_AND_DIALOG_HEIGHT_DIFFERENCE = 60;
    private static final int DIAGRAM_HEIGHT = DIALOG_HEIGHT - DIAGRAM_AND_DIALOG_HEIGHT_DIFFERENCE;
    private static final int DIAGRAM_WIDTH = 550;

    private static final int COLS = 2;

    private static final Color FIELD_COLOR = new Color(25, 114, 120);
    private static final Color METHOD_COLOR = new Color(196, 69, 54);
    private static final Color CONSTRUCTOR_COLOR = new Color(119, 46, 37);

    private static final String CLASS_TEXT_COLOR = "EDDDD4";

    private static final String FIELD_TITLE = "field";
    private static final String METHOD_TITLE = "method";
    private static final String CONSTRUCTOR_TITLE = "constructor";

    private String className;
    private ClassOrInterfaceUnit classUnit;
    private PrettyPrinter prettyPrinter;

    private JDialog dialog;
    private JPanel panel;
    private JPanel schemaPanel;
    private JPanel metricsPanel;
    private JLabel label;
    private JScrollPane scrollPaneForDiagram;
    private JButton classNameButton;
    private JScrollPane scrollPaneForUnitText;
    private JTextArea unitText;
    private JTable metricsTable;
    private DefaultTableModel metricsTableModel;
    private JScrollPane scrollPaneForJavaMetricsTable;
    private List<JButton> classElementsButtons;


    public ClassDialog(String className, ClassOrInterfaceUnit classUnit, Frame owner) {
        this.className = className;
        this.classUnit = classUnit;
        this.dialog = new JDialog(owner);
        this.prettyPrinter = new PrettyPrinter();
        initDialogElements();
    }

    public void show() {
        dialog.setVisible(true);
    }

    private void initDialogElements() {
        classElementsButtons = new ArrayList<>();

        panel = new JPanel(new FlowLayout());
        panel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);


        metricsPanel = new JPanel();
        metricsPanel.setLayout(new BoxLayout(metricsPanel, BoxLayout.Y_AXIS));

        label = new JLabel(className);
        metricsPanel.add(label);


        classNameButton = new JButton(className);
        setOnButtonClickListener(classNameButton, classUnit, className, classUnit.getClassOrInterfaceDeclaration());


        metricsTableModel = new NoneEditableTableModel();
        metricsTableModel.addColumn("Метрика");
        metricsTableModel.addColumn("Величина");
        updateMetricsTable(classUnit);

        metricsTable = new JTable(metricsTableModel);

        scrollPaneForJavaMetricsTable = new JScrollPane(metricsTable);
        metricsPanel.add(scrollPaneForJavaMetricsTable);

        for (FieldUnit unit : classUnit.getFieldUnits()) {
            JButton button = null;
            //String title =  unit.getFieldDeclaration().getVariables().get(0).getNameAsString();
            String title = unit.getFieldDeclaration().toString();
            button = new JButton(
                    TextColorUtils.getColorStringWithSupText(
                            title,
                            CLASS_TEXT_COLOR,
                            FIELD_TITLE,
                            CLASS_TEXT_COLOR));
            button.setBackground(FIELD_COLOR);
            button.setBorderPainted(false);

            setOnButtonClickListener(button, unit, title, unit.getFieldDeclaration());
            classElementsButtons.add(button);
        }

        for (ConstructorUnit unit : classUnit.getConstructorUnits()) {
            JButton button = null;
            String title = unit.getConstructorDeclaration().getDeclarationAsString(true, true, true);
            button = new JButton(
                    TextColorUtils.getColorStringWithSupText(
                            title,
                            CLASS_TEXT_COLOR,
                            CONSTRUCTOR_TITLE,
                            CLASS_TEXT_COLOR));
            button.setBackground(CONSTRUCTOR_COLOR);
            button.setBorderPainted(false);
            setOnButtonClickListener(button, unit, title, unit.getConstructorDeclaration());
            classElementsButtons.add(button);
        }

        for (MethodUnit unit : classUnit.getMethodUnits()) {
            JButton button = null;
            String title = unit.getMethodDeclaration().getDeclarationAsString(true, true, true);
            button = new JButton(
                    TextColorUtils.getColorStringWithSupText(
                            title,
                            CLASS_TEXT_COLOR,
                            METHOD_TITLE,
                            CLASS_TEXT_COLOR));
            button.setBackground(METHOD_COLOR);
            button.setBorderPainted(false);
            setOnButtonClickListener(button, unit, title, unit.getMethodDeclaration());
            classElementsButtons.add(button);
        }

//        int cols = classElementsButtons.size() >= 2 ? 2 : 1;
        GridLayout gridLayout = new GridLayout(0, 1, 10, 10);

        schemaPanel = new ClassDiagramPanel(classElementsButtons, gridLayout, classNameButton);
        schemaPanel.setBackground(Color.WHITE);

        JPanel diagramPanel = new JPanel();
        diagramPanel.add(schemaPanel);

        scrollPaneForDiagram = new JScrollPane(diagramPanel);
        scrollPaneForDiagram.setPreferredSize(new Dimension(DIAGRAM_WIDTH, DIAGRAM_HEIGHT));
        scrollPaneForDiagram.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPaneForDiagram.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);


        schemaPanel.add(classNameButton);
        classElementsButtons.forEach(schemaPanel::add);

        panel.add(scrollPaneForDiagram);
        panel.add(metricsPanel);

        initTextArea();
        panel.add(scrollPaneForUnitText);


        dialog.setSize(DIALOG_WIDTH, DIALOG_HEIGHT);
        dialog.add(panel);
        dialog.setTitle(className);

        dialog.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                int scrollPaneHeight = dialog.getHeight() > DIAGRAM_AND_DIALOG_HEIGHT_DIFFERENCE ?
                        dialog.getHeight() - DIAGRAM_AND_DIALOG_HEIGHT_DIFFERENCE : 0;
                scrollPaneForDiagram.setPreferredSize(new Dimension(DIAGRAM_WIDTH, scrollPaneHeight));
            }
        });
    }

    private void updateMetricsTable(Unit unit) {
        metricsTableModel.setRowCount(0);
        unit.getMetrics().stream()
                .sorted(new MetricsBySpecificityComparator())
                .forEach(metric ->
                metricsTableModel.addRow(new Object[]
                        {
                                metric.getTitle(),
                                metric.getCount()
                        })
        );
    }

    private void initTextArea(){
        unitText = new JTextArea();
        updateTextArea(classUnit.getClassOrInterfaceDeclaration());

        Font font = new Font("Courier New", Font.PLAIN, 14);
        unitText.setFont(font);

        scrollPaneForUnitText = new JScrollPane(unitText);
        scrollPaneForUnitText.setPreferredSize(new Dimension(DIAGRAM_WIDTH, DIAGRAM_HEIGHT));
        scrollPaneForUnitText.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPaneForUnitText.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        clearBorder(unitText);
        clearBorder(scrollPaneForUnitText);
    }

    private void clearBorder(JComponent component){
        component.setBorder(new LineBorder(Color.BLACK, 0));
    }

    private void updateLabel(String title) {
        label.setText(title);
    }

    private void setOnButtonClickListener(JButton button, Unit unit, String labelTitle, Node node) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    updateMetricsTable(unit);
                    updateLabel(labelTitle);
                    updateTextArea(node);
                }
            }
        });
    }

    private void updateTextArea(Node node) {
        unitText.setText(prettyPrinter.print(node));
    }

}


