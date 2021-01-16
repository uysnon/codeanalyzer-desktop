package ru.rsreu.gorkin.codeanalyzer.metrics.base;

import com.github.javaparser.Position;
import com.github.javaparser.Range;
import com.github.javaparser.ast.Node;
import ru.rsreu.gorkin.codeanalyzer.metrics.Metrics;

public class CodeLineMetrics extends Metrics<Node> {
    private static final String TITLE = "Количество строк кода";
    private static final String DESCRIPTION = "Количество строк кода, занимаемое элементом";

    @Override
    public void process(Node node) {
        setCount(node.getRange().orElse(
                new Range(
                        new Position(0, 0),
                        new Position(0, 0)))
                .getLineCount());
        setTitle(TITLE);
        setDescription(DESCRIPTION);
    }
}
