package ru.rsreu.gorkin.codeanalyzer.syntaxelements;

import com.github.javaparser.ast.body.EnumDeclaration;
import ru.rsreu.gorkin.codeanalyzer.metrics.Metrics;
import ru.rsreu.gorkin.codeanalyzer.metrics.enums.CountElementsEnumMetrics;

public class EnumUnit extends Unit {
    private EnumDeclaration enumDeclaration;

    public EnumUnit(EnumDeclaration enumDeclaration) {
        this.enumDeclaration = enumDeclaration;
        addAllMetrics();
    }

    @Override
    public void calculateMetrics() {
        for (Metrics metric : getMetrics()) {
            metric.process(enumDeclaration);
        }
    }

    @Override
    protected void addCustomUnitMetrics() {
        addMetric(new CountElementsEnumMetrics());
    }
}
