package ru.rsreu.gorkin.codeanalyzer.metrics.method;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import ru.rsreu.gorkin.codeanalyzer.metrics.utils.BlockStatementsCounter;

public class SwitchStatementsMethodMetric extends MethodMetric {
    private static final String TITLE = "switch выражения";
    private static final String DESCRIPTION = "Количество switch выражений";

    public SwitchStatementsMethodMetric() {
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
            count = new BlockStatementsCounter().calculateSwitchStatements(blockStmt);
        }
        setCount(count);
    }
}
