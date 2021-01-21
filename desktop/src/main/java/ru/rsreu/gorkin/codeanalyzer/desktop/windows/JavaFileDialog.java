package ru.rsreu.gorkin.codeanalyzer.desktop.windows;

import ru.rsreu.gorkin.codeanalyzer.core.syntaxelements.ClassOrInterfaceUnit;
import ru.rsreu.gorkin.codeanalyzer.core.syntaxelements.EnumUnit;
import ru.rsreu.gorkin.codeanalyzer.core.syntaxelements.SourceCodeUnit;
import ru.rsreu.gorkin.codeanalyzer.desktop.elements.panels.ClassDiagramPanel;
import ru.rsreu.gorkin.codeanalyzer.desktop.elements.utils.TextColorUtils;

import javax.swing.*;
import java.awt.*;
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

    private JDialog dialog;
    private JPanel panel;
    private JPanel schemaPanel;
    private JLabel label;

    private List<JButton> classes;


    public JavaFileDialog(String fileName, SourceCodeUnit sourceCodeUnit, Frame owner) {
        this.fileName = fileName;
        this.sourceCodeUnit = sourceCodeUnit;
        this.dialog = new JDialog(owner);
        initDialogElements();
    }

    public void show() {
        dialog.setVisible(true);
    }

    private void initDialogElements() {
        classes = new ArrayList<>();

        schemaPanel = new ClassDiagramPanel(classes);

        panel = new JPanel();
        label = new JLabel(fileName);

        for (ClassOrInterfaceUnit unit : sourceCodeUnit.getClassOrInterfaceUnits()) {
            JButton button = null;
            if (unit.getClassOrInterfaceDeclaration().isInterface()) {
                button = new JButton(
                        TextColorUtils.getColorStringWithSupText(
                                unit.getClassOrInterfaceDeclaration().getNameAsString(),
                                CLASS_TEXT_COLOR,
                                INTERFACE_TITLE,
                                CLASS_TEXT_COLOR));
                button.setBackground(interfaceColor);
            } else {
                button = new JButton(
                        TextColorUtils.getColorStringWithSupText(
                                unit.getClassOrInterfaceDeclaration().getNameAsString(),
                                CLASS_TEXT_COLOR,
                                CLASS_TITLE,
                                CLASS_TEXT_COLOR));
                button.setBackground(classColor);
            }
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

        classes.forEach(schemaPanel::add);
        panel.add(label);
        panel.add(schemaPanel);


        dialog.setSize(600, 500);
        dialog.add(panel);
        dialog.setTitle(fileName);
    }

}
