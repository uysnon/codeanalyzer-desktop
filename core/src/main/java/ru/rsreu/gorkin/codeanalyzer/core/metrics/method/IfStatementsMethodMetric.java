package ru.rsreu.gorkin.codeanalyzer.core.metrics.method;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import ru.rsreu.gorkin.codeanalyzer.core.metrics.utils.BlockStatementsCounter;

public class IfStatementsMethodMetric extends MethodMetric {
    private static final String TITLE = "if выражения";
    private static final String DESCRIPTION = "Количество if выражений";

    public IfStatementsMethodMetric() {
        setTitle(TITLE);
        setDescription(DESCRIPTION);
    }

    @Override
    public void process(MethodDeclaration methodDeclaration) {
        BlockStmt blockStmt = methodDeclaration.getChildNodes()
                .stream()
                .filter(node -> node instanceof BlockStmt)
                .map(node -> (BlockStmt) node)
                .findAny()
                .orElse(null);
        int count = 0;
        if (blockStmt != null) {
            count = new BlockStatementsCounter().calculateIfStatements(blockStmt);
        }
        setCount(count);
    }
}
