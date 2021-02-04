package ru.rsreu.gorkin.codeanalyzer.core.metrics.constructor;

import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import ru.rsreu.gorkin.codeanalyzer.core.metrics.utils.BlockStatementsCounter;

public class SwitchStatementsConstructorMetric extends ConstructorMetric {
    private static final String TITLE = "switch выражения";
    private static final String DESCRIPTION = "Количество switch выражений";

    public SwitchStatementsConstructorMetric() {
        setTitle(TITLE);
        setDescription(DESCRIPTION);
    }

    @Override
    public void process(ConstructorDeclaration constructorDeclaration) {
        BlockStmt blockStmt = constructorDeclaration.getChildNodes()
                .stream()
                .filter(node -> node instanceof BlockStmt)
                .map(node -> (BlockStmt) node)
                .findAny()
                .orElse(null);
        int count = 0;
        if (blockStmt != null) {
            count = new BlockStatementsCounter().calculateRecursiveSwitchStatements(blockStmt);
        }
        setCount(count);
    }
}
