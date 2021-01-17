package ru.rsreu.gorkin.codeanalyzer.syntaxelements;

import com.github.javaparser.ast.body.MethodDeclaration;
import ru.rsreu.gorkin.codeanalyzer.metrics.Metric;
import ru.rsreu.gorkin.codeanalyzer.metrics.method.IfStatementsMethodMetric;
import ru.rsreu.gorkin.codeanalyzer.metrics.method.SwitchStatementsMethodMetric;

public class MethodUnit extends Unit {
    private MethodDeclaration methodDeclaration;


    public MethodUnit(MethodDeclaration methodDeclaration) {
        this.methodDeclaration = methodDeclaration;
        addAllMetrics();
    }

    @Override
    protected void addCustomUnitMetrics() {
        addMetrics(new IfStatementsMethodMetric(),
                new SwitchStatementsMethodMetric());
    }

    @Override
    public void calculateMetrics() {
        for (Metric metric : getMetrics()) {
            metric.process(methodDeclaration);
        }
    }


}
