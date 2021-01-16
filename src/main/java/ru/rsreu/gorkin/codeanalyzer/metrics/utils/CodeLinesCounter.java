package ru.rsreu.gorkin.codeanalyzer.metrics.utils;

import com.github.javaparser.Position;
import com.github.javaparser.Range;
import com.github.javaparser.ast.Node;

public class CodeLinesCounter {
    public int getCountCodeLines(Node node){
        return node.getRange().orElse(
                new Range(
                        new Position(0, 0),
                        new Position(0, 0)))
                .getLineCount();
    }
}
