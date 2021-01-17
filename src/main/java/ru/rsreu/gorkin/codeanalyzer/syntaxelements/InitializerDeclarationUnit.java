package ru.rsreu.gorkin.codeanalyzer.syntaxelements;

import com.github.javaparser.ast.body.InitializerDeclaration;
import ru.rsreu.gorkin.codeanalyzer.metrics.Metric;
import ru.rsreu.gorkin.codeanalyzer.metrics.initializerdeclaration.IfStatementsInitializerDeclarationMetric;
import ru.rsreu.gorkin.codeanalyzer.metrics.initializerdeclaration.SwitchStatementsInitializerDeclarationMetric;

public class InitializerDeclarationUnit extends Unit {
    private InitializerDeclaration initializerDeclaration;

    public InitializerDeclarationUnit(InitializerDeclaration initializerDeclaration) {
        this.initializerDeclaration = initializerDeclaration;
        addAllMetrics();
    }

    @Override
    public void calculateMetrics() {
        for (Metric metric : getMetrics()) {
            metric.process(initializerDeclaration);
        }
    }

    @Override
    protected void addCustomUnitMetrics() {
        addMetrics(new IfStatementsInitializerDeclarationMetric(),
                new SwitchStatementsInitializerDeclarationMetric());
    }
}
