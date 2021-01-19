package ru.rsreu.gorkin.codeanalyzer.desktop.elements.files;

import ru.rsreu.gorkin.codeanalyzer.desktop.elements.files.fileutils.ExtensionUtils;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;

public class JavaFilesChooser extends JFileChooser {
    public JavaFilesChooser() {
        super("D:\\afterschool\\RSEU\\4 course\\7 sem\\javaWeb\\labs\\labSupermarket\\maven-project\\");
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
