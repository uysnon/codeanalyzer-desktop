package ru.rsreu.gorkin.codeanalyzer.core.metrics.initializerdeclaration;

import com.github.javaparser.ast.body.InitializerDeclaration;
import ru.rsreu.gorkin.codeanalyzer.core.metrics.Metric;

public abstract class InitializerDeclarationMetric extends Metric<InitializerDeclaration> {
    private static final int SPECIFICITY_RATE = 2;

    public InitializerDeclarationMetric() {
        setSpecificityRate(SPECIFICITY_RATE);
    }
}
