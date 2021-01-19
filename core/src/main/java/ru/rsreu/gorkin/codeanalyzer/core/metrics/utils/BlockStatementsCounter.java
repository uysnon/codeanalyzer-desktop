package ru.rsreu.gorkin.codeanalyzer.core.metrics.utils;

import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.SwitchStmt;

public class BlockStatementsCounter {
    public int calculateIfStatements(BlockStmt blockStmt) {
        return (int) blockStmt.getStatements().stream()
                .filter(statement -> statement instanceof IfStmt)
                .count();
    }

    public int calculateSwitchStatements(BlockStmt blockStmt) {
        return (int) blockStmt.getStatements().stream()
                .filter(statement -> statement instanceof SwitchStmt)
                .count();
    }
}
