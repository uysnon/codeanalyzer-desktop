package ru.rsreu.gorkin.codeanalyzer.core.rules;

import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import ru.rsreu.gorkin.codeanalyzer.core.syntaxelements.FieldUnit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class FieldNamingRule implements Rule {
    private List<FieldUnit> fieldUnits;
    private String namingPattern;

    public List<FieldUnit> getFieldUnits() {
        return fieldUnits;
    }

    public void setFieldUnits(List<FieldUnit> fieldUnits) {
        this.fieldUnits = fieldUnits;
    }

    public String getNamingPattern() {
        return namingPattern;
    }

    public void setNamingPattern(String namingPattern) {
        this.namingPattern = namingPattern;
    }

    public FieldNamingRule(String namingPattern) {
        this.namingPattern = namingPattern;
        fieldUnits = new ArrayList<>();
    }

    public FieldNamingRule(List<FieldUnit> fieldUnits, String namingPattern) {
        this.namingPattern = namingPattern;
        this.fieldUnits = fieldUnits;
    }

    public void addField(FieldUnit unit) {
        fieldUnits.add(unit);
    }

    public void addFields(Collection<FieldUnit> fields) {
        fieldUnits.addAll(fields);
    }

    @Override
    public ValidationResult validate() {
        String resultDescription = "";
        boolean resultValue = true;
        for (FieldUnit unit : fieldUnits) {
            if (!unit.isStatic()) {
                List<String> variableNames = unit.getFieldDeclaration()
                        .getVariables()
                        .stream()
                        .map(NodeWithSimpleName::getNameAsString)
                        .collect(Collectors.toList());
                for (String variableName : variableNames) {
                    ValidationResult iterationResult = validate(variableName);
                    if (!iterationResult.isNormal()) {
                        if (resultValue) {
                            resultValue = false;
                        }
                        resultDescription += iterationResult.getDescription() + "\n";
                    }
                }
            }
        }
        if (resultDescription == "") {
            resultDescription = "OK";
        }
        return new ValidationResult(resultValue, resultDescription);
    }

    private ValidationResult validate(String fieldName) {
        ValidationResult result = null;
        if (fieldName.matches(namingPattern)) {
            result = new ValidationResult(true, String.format("OK - %s", fieldName));
        } else {
            result = new ValidationResult(false, String.format("ERROR - %s", fieldName));
        }
        return result;
    }

}
