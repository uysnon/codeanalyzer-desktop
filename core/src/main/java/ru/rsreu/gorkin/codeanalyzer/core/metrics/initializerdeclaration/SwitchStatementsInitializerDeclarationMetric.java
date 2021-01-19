package ru.rsreu.gorkin.codeanalyzer.core.metrics.initializerdeclaration;

import com.github.javaparser.ast.body.InitializerDeclaration;
import com.github.javaparser.ast.stmt.BlockStmt;
import ru.rsreu.gorkin.codeanalyzer.core.metrics.utils.BlockStatementsCounter;

public class SwitchStatementsInitializerDeclarationMetric extends InitializerDeclarationMetric{
    private static final String TITLE = "switch выражения";
    private static final String DESCRIPTION = "Количество switch выражений";

    public SwitchStatementsInitializerDeclarationMetric() {
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
            count = new BlockStatementsCounter().calculateSwitchStatements(blockStmt);
        }
        setCount(count);
    }
}
