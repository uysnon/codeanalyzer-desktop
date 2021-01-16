package ru.rsreu.gorkin.codeanalyzer.syntaxelements;

import com.github.javaparser.ast.body.MethodDeclaration;
import ru.rsreu.gorkin.codeanalyzer.metrics.Metrics;

public class MethodUnit extends Unit {
    private MethodDeclaration methodDeclaration;


    public MethodUnit(MethodDeclaration methodDeclaration) {
        this.methodDeclaration = methodDeclaration;
        addAllMetrics();
    }

    @Override
    protected void addCustomUnitMetrics() {

    }

    @Override
    public void calculateMetrics() {
        for (Metrics metric : getMetrics()) {
            metric.process(methodDeclaration);
        }
    }


}
