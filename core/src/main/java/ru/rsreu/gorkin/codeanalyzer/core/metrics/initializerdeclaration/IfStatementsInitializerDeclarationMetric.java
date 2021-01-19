package ru.rsreu.gorkin.codeanalyzer.core.metrics.initializerdeclaration;

import com.github.javaparser.ast.body.InitializerDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import ru.rsreu.gorkin.codeanalyzer.core.metrics.utils.BlockStatementsCounter;

public class IfStatementsInitializerDeclarationMetric extends InitializerDeclarationMetric{
    private static final String TITLE = "if выражения";
    private static final String DESCRIPTION = "Количество if выражений";

    public IfStatementsInitializerDeclarationMetric() {
        setTitle(TITLE);
        setDescription(DESCRIPTION);
    }

    @Override
    public void process(InitializerDeclaration initializerDeclaration) {
        BlockStmt blockStmt = initializerDeclaration.getChildNodes()
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
