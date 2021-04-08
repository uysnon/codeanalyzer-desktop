package ru.rsreu.gorkin.codeanalyzer.desktop;

import ru.rsreu.gorkin.codeanalyzer.core.syntaxelements.SourceCodeUnit;
import ru.rsreu.gorkin.codeanalyzer.desktop.forms.MainForm;
import ru.rsreu.gorkin.codeanalyzer.desktop.forms.ProjectForm;
import ru.rsreu.gorkin.codeanalyzer.desktop.forms.ScanByRulesForm;
import ru.rsreu.gorkin.codeanalyzer.desktop.forms.SourceFileForm;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Map;

public class NewSwingApp {
    private static final int WINDOW_HEIGHT = 540;
    private static final int WINDOW_WIDTH = 900;
    private Map<File, SourceCodeUnit> fileSourceCodeUnitMap;

    private MainForm mainForm;
    private ProjectForm projectForm;
    private ScanByRulesForm scanByRulesForm;

    public NewSwingApp() {
        mainForm = new MainForm();
        projectForm = new ProjectForm();
        projectForm.setProjectStructureConsumer(map -> fileSourceCodeUnitMap = map);

        projectForm.setClickSourceCodeUnitConsumer(unit ->
        {
            SourceFileForm form = new SourceFileForm(unit);
            mainForm.addCloseableTab("Файл", form.getParentPanel());
        });
        scanByRulesForm = new ScanByRulesForm();
        mainForm.addNoneCloseableTab("Проект", projectForm.getParentPanel());
        mainForm.addNoneCloseableTab("Проверка", scanByRulesForm.getParentPanel());
    }

    public static void main(String[] args) {
        NewSwingApp newSwingApp = new NewSwingApp();
        newSwingApp.run();
    }

    public void run() {
        JFrame frame = new JFrame("CodeAnalyzer");
        frame.setMinimumSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        frame.add(mainForm.getParentPanel());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }


}
