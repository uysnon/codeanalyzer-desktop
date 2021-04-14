package ru.rsreu.gorkin.codeanalyzer.desktop.ui.elements.files;

import ru.rsreu.gorkin.codeanalyzer.desktop.ui.elements.files.fileutils.ExtensionUtils;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;

public class JavaFilesChooser extends JFileChooser {
    private static final String basePath = "D:\\afterschool\\RSEU\\4 course\\8 sem\\управление программными проектами\\project\\science_competition";
    public JavaFilesChooser() {
        super(basePath);
        setMultiSelectionEnabled(true);
        setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        setAcceptAllFileFilterUsed(false);
        setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory()) {
                    return true;
                }
                return ExtensionUtils.isJavaFile(f);
            }

            @Override
            public String getDescription() {
                return "Java files/directories";
            }
        });
    }



}
