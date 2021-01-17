package ru.rsreu.gorkin.codeanalyzer.metrics;

import com.github.javaparser.ast.Node;

import java.util.Objects;

public abstract class Metric<T extends Node> {
    private int count;
    private String title;
    private String description;

    public Metric() {
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public abstract void process(T t);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Metric)) return false;
        Metric metric = (Metric) o;
        return count == metric.count &&
                Objects.equals(title, metric.title) &&
                Objects.equals(description, metric.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(count, title, description);
    }

    @Override
    public String toString() {
        return "Metrics{" +
                "count=" + count +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
