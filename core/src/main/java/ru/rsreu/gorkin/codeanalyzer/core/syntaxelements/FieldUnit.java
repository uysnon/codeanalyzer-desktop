package ru.rsreu.gorkin.codeanalyzer.core.syntaxelements;

import com.github.javaparser.ast.body.FieldDeclaration;
import ru.rsreu.gorkin.codeanalyzer.core.metrics.Metric;

public class FieldUnit extends Unit {
    private FieldDeclaration fieldDeclaration;

    public FieldUnit(FieldDeclaration fieldDeclaration) {
        this.fieldDeclaration = fieldDeclaration;
        addAllMetrics();
    }

    public FieldDeclaration getFieldDeclaration() {
        return fieldDeclaration;
    }

    public void setFieldDeclaration(FieldDeclaration fieldDeclaration) {
        this.fieldDeclaration = fieldDeclaration;
    }

    @Override
    public void calculateMetrics() {
        for (Metric metric : getMetrics()) {
            metric.process(fieldDeclaration);
        }
    }

    @Override
    protected void addCustomUnitMetrics() {

    }
}
