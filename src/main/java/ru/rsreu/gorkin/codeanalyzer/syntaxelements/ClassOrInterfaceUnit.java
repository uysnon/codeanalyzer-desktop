package ru.rsreu.gorkin.codeanalyzer.syntaxelements;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import ru.rsreu.gorkin.codeanalyzer.metrics.Metric;
import ru.rsreu.gorkin.codeanalyzer.metrics.classorinterface.FieldsClassMetric;
import ru.rsreu.gorkin.codeanalyzer.metrics.classorinterface.InitializerDeclarationsClassMetric;
import ru.rsreu.gorkin.codeanalyzer.metrics.classorinterface.MethodsClassMetric;
import ru.rsreu.gorkin.codeanalyzer.metrics.classorinterface.StaticInitializerDeclarationsClassMetric;

import java.util.ArrayList;
import java.util.List;

public class ClassOrInterfaceUnit extends Unit {
    private ClassOrInterfaceDeclaration classOrInterfaceDeclaration;
    private List<MethodUnit> methodUnits;
    private List<InitializerDeclarationUnit> initializerDeclarationUnits;

    public ClassOrInterfaceUnit(ClassOrInterfaceDeclaration classOrInterfaceDeclaration) {
        this.classOrInterfaceDeclaration = classOrInterfaceDeclaration;
        this.methodUnits = new ArrayList<>();
        this.initializerDeclarationUnits = new ArrayList<>();
        addAllMetrics();
    }

    public ClassOrInterfaceDeclaration getClassOrInterfaceDeclaration() {
        return classOrInterfaceDeclaration;
    }

    public void setClassOrInterfaceDeclaration(ClassOrInterfaceDeclaration classOrInterfaceDeclaration) {
        this.classOrInterfaceDeclaration = classOrInterfaceDeclaration;
    }

    public List<MethodUnit> getMethodUnits() {
        return methodUnits;
    }

    public void setMethodUnits(List<MethodUnit> methodUnits) {
        this.methodUnits = methodUnits;
    }

    public List<InitializerDeclarationUnit> getInitializerDeclarationUnits() {
        return initializerDeclarationUnits;
    }

    public void setInitializerDeclarationUnits(List<InitializerDeclarationUnit> initializerDeclarationUnits) {
        this.initializerDeclarationUnits = initializerDeclarationUnits;
    }

    @Override
    public void calculateMetrics() {
        for (Metric metric : getMetrics()) {
            metric.process(classOrInterfaceDeclaration);
        }
        methodUnits.forEach(MethodUnit::calculateMetrics);
        initializerDeclarationUnits.forEach(
                InitializerDeclarationUnit::calculateMetrics
        );
    }

    @Override
    protected void addCustomUnitMetrics() {
        addMetrics(new FieldsClassMetric(),
                new MethodsClassMetric(),
                new InitializerDeclarationsClassMetric(),
                new StaticInitializerDeclarationsClassMetric());
    }
}
