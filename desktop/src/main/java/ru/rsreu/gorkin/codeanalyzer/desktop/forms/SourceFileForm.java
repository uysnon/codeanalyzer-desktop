package ru.rsreu.gorkin.codeanalyzer.desktop.forms;

import ru.rsreu.gorkin.codeanalyzer.core.syntaxelements.ClassOrInterfaceUnit;
import ru.rsreu.gorkin.codeanalyzer.core.syntaxelements.EnumUnit;
import ru.rsreu.gorkin.codeanalyzer.core.syntaxelements.SourceCodeUnit;
import ru.rsreu.gorkin.codeanalyzer.desktop.elements.panels.DiagramPanel;
import ru.rsreu.gorkin.codeanalyzer.desktop.elements.panels.FileDiagramPanel;
import ru.rsreu.gorkin.codeanalyzer.desktop.elements.utils.TextColorUtils;
import ru.rsreu.gorkin.codeanalyzer.desktop.frames.ClassDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class SourceFileForm extends UnitForm {
    public static final Color FILE_COLOR = new Color(25, 114, 120);
    public static final Color CLASS_COLOR = new Color(196, 69, 54);
    public static final Color INTERFACE_COLOR = new Color(119, 46, 37);
    public static final Color ENUM_COLOR = new Color(119, 46, 37);
    public static final String HEX_LIGHT_COLOR = "EDDDD4";
    private static final String ENUM_TITLE = "enum";
    private static final String CLASS_TITLE = "class";
    private static final String INTERFACE_TITLE = "interface";

    private JPanel parentPanel;
    private JTabbedPane sourceCodePanel;
    private JButton exportDiagramButton;
    private JPanel diagramPanel;
    private JPanel metricsPanel;
    private JPanel programTextPanel;
    private JTable metricsTable;
    private JTextArea unitTextArea;
    private JButton exportMetricsButton;
    private JLabel unitLabel;
    private JScrollPane diagramScrollPane;
    private JPanel diagramParentPanel;

    private List<JButton> units;
    private SourceCodeUnit sourceCodeUnit;


    public SourceFileForm(SourceCodeUnit sourceCodeUnit) {
        this.sourceCodeUnit  = sourceCodeUnit;
        this.units = new ArrayList<>();
        createUIComponents();
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

    private void createUIComponents() {
        init();
        updateMetricsTable(sourceCodeUnit);
        updateTextArea(sourceCodeUnit.getCompilationUnit());
        initDiagramPanel();
    }

    private void initDiagramPanel() {
        for (ClassOrInterfaceUnit unit : sourceCodeUnit.getClassOrInterfaceUnits()) {
            JButton button = null;
            String name = unit.getClassOrInterfaceDeclaration().getNameAsString();
            if (unit.getClassOrInterfaceDeclaration().isInterface()) {
                button = new JButton(
                        TextColorUtils.getColorStringWithSupText(
                                name,
                                HEX_LIGHT_COLOR,
                                INTERFACE_TITLE,
                                HEX_LIGHT_COLOR));
                button.setBackground(INTERFACE_COLOR);

            } else {
                button = new JButton(
                        TextColorUtils.getColorStringWithSupText(
                                name,
                                HEX_LIGHT_COLOR,
                                CLASS_TITLE,
                                HEX_LIGHT_COLOR));
                button.setBackground(CLASS_COLOR);
            }
            button.addMouseListener(
                    new MouseAdapter() {
                        String title = unit.getClassOrInterfaceDeclaration().toString();

                        @Override
                        public void mousePressed(MouseEvent e) {
                            if (e.getClickCount() == 1) {
                                onOnceClickListener(unit, title, unit.getClassOrInterfaceDeclaration());
                            } else {
                                if (e.getClickCount() == 2) {
                                    ClassDialog classDialog = new ClassDialog(
                                            name,
                                            unit,
                                            null);
                                    classDialog.show();
                                }
                            }
                        }
                    }
            );
            button.setBorderPainted(false);
            units.add(button);
        }

        for (EnumUnit unit : sourceCodeUnit.getEnumUnits()) {
            JButton button = new JButton(
                    TextColorUtils.getColorStringWithSupText(
                            unit.getEnumDeclaration().getNameAsString(),
                            HEX_LIGHT_COLOR,
                            ENUM_TITLE,
                            HEX_LIGHT_COLOR));
            button.setBackground(ENUM_COLOR);
            button.setBorderPainted(false);
            units.add(button);
        }

        int cols = units.size() >= 2 ? 2 : 1;
        GridLayout gridLayout = new GridLayout(5, 3, 5, 5);

        sourceCodeUnit.getFileName();
        Button sourceCodeUnitButton = new Button(sourceCodeUnit.getFileName());
        diagramPanel = new DiagramPanel(units, gridLayout, sourceCodeUnitButton);

        diagramPanel.add(sourceCodeUnitButton);
        units.forEach(diagramPanel::add);

        diagramParentPanel.add(diagramPanel);


    }


}
