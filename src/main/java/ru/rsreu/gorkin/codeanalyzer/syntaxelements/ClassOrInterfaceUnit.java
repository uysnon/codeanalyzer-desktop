package ru.rsreu.gorkin.codeanalyzer.syntaxelements;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import ru.rsreu.gorkin.codeanalyzer.metrics.Metrics;

import java.util.ArrayList;
import java.util.List;

public class ClassOrInterfaceUnit extends Unit {
    private ClassOrInterfaceDeclaration classOrInterfaceDeclaration;
    private List<MethodUnit> methods;

    public ClassOrInterfaceUnit(ClassOrInterfaceDeclaration classOrInterfaceDeclaration) {
        this.classOrInterfaceDeclaration = classOrInterfaceDeclaration;
        this.methods = new ArrayList<>();
        addAllMetrics();
    }

    public ClassOrInterfaceDeclaration getClassOrInterfaceDeclaration() {
        return classOrInterfaceDeclaration;
    }

    public void setClassOrInterfaceDeclaration(ClassOrInterfaceDeclaration classOrInterfaceDeclaration) {
        this.classOrInterfaceDeclaration = classOrInterfaceDeclaration;
    }

    public List<MethodUnit> getMethods() {
        return methods;
    }

    public void setMethods(List<MethodUnit> methods) {
        this.methods = methods;
    }


    @Override
    public void calculateMetrics() {
        for (Metrics metric : getMetrics()) {
            metric.process(classOrInterfaceDeclaration);
        }
        methods.forEach(MethodUnit::calculateMetrics);
    }

    @Override
    protected void addCustomUnitMetrics() {

    }
}
