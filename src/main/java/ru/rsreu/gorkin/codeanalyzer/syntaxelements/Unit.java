package ru.rsreu.gorkin.codeanalyzer.syntaxelements;

import ru.rsreu.gorkin.codeanalyzer.metrics.Metrics;
import ru.rsreu.gorkin.codeanalyzer.metrics.base.CodeLineMetrics;

import java.util.ArrayList;
import java.util.List;


public abstract class Unit {
    private List<Metrics> metrics;

    public Unit() {
        this.metrics = new ArrayList<>();
    }

    public List<Metrics> getMetrics() {
        return metrics;
    }

    public void setMetrics(List<Metrics> metrics) {
        this.metrics = metrics;
    }

    public abstract void calculateMetrics();

    public void addAllMetrics() {
        addBaseUnitMetrics();
        addCustomUnitMetrics();
    }

    protected void addMetric(Metrics metric){
        if (metrics != null){
            metrics.add(metric);
        }
    }

    protected abstract void addCustomUnitMetrics();

    protected void addBaseUnitMetrics() {
        metrics.add(new CodeLineMetrics());
    }
}
