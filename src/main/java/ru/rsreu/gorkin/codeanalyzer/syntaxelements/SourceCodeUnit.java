package ru.rsreu.gorkin.codeanalyzer.syntaxelements;

import com.github.javaparser.ast.CompilationUnit;
import ru.rsreu.gorkin.codeanalyzer.metrics.Metric;

import java.util.ArrayList;
import java.util.List;

public class SourceCodeUnit extends Unit {
    private CompilationUnit compilationUnit;
    private List<ClassOrInterfaceUnit> classOrInterfaceUnits;
    private List<EnumUnit> enumUnits;

    public SourceCodeUnit(CompilationUnit compilationUnit) {
        this.compilationUnit = compilationUnit;
        classOrInterfaceUnits = new ArrayList<>();
        enumUnits = new ArrayList<>();
        addAllMetrics();
    }

    public CompilationUnit getCompilationUnit() {
        return compilationUnit;
    }

    public void setCompilationUnit(CompilationUnit compilationUnit) {
        this.compilationUnit = compilationUnit;
    }

    public List<ClassOrInterfaceUnit> getClassOrInterfaceUnits() {
        return classOrInterfaceUnits;
    }

    public void setClassOrInterfaceUnits(List<ClassOrInterfaceUnit> classOrInterfaceUnits) {
        this.classOrInterfaceUnits = classOrInterfaceUnits;
    }

    public List<EnumUnit> getEnumUnits() {
        return enumUnits;
    }

    public void setEnumUnits(List<EnumUnit> enumUnits) {
        this.enumUnits = enumUnits;
    }

    @Override
    public void calculateMetrics() {
        for (Metric metric : getMetrics()) {
            metric.process(compilationUnit);
        }
        classOrInterfaceUnits.forEach(ClassOrInterfaceUnit::calculateMetrics);
        enumUnits.forEach(EnumUnit::calculateMetrics);
    }

    @Override
    protected void addCustomUnitMetrics() {

    }
}
