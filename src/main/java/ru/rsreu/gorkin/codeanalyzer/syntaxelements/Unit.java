package ru.rsreu.gorkin.codeanalyzer.syntaxelements;

import ru.rsreu.gorkin.codeanalyzer.metrics.Metric;
import ru.rsreu.gorkin.codeanalyzer.metrics.base.CodeLinesMetric;

import java.util.ArrayList;
import java.util.List;


public abstract class Unit {
    private List<Metric> metrics;

    public Unit() {
        this.metrics = new ArrayList<>();
    }

    public List<Metric> getMetrics() {
        return metrics;
    }

    public void setMetrics(List<Metric> metrics) {
        this.metrics = metrics;
    }

    public abstract void calculateMetrics();

    public void addAllMetrics() {
        addBaseUnitMetrics();
        addCustomUnitMetrics();
    }

    protected void addMetric(Metric metric) {
        if (metrics != null) {
            metrics.add(metric);
        }
    }

    protected void addMetrics(Metric... metrics) {
        for (Metric metric : metrics) {
            addMetric(metric);
        }
    }

    protected abstract void addCustomUnitMetrics();

    protected void addBaseUnitMetrics() {
        metrics.add(new CodeLinesMetric());
    }
}
