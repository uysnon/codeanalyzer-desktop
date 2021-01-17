package ru.rsreu.gorkin.codeanalyzer.adapters;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.InitializerDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import ru.rsreu.gorkin.codeanalyzer.syntaxelements.*;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class TreeCreator {
    public ProjectTree create(Collection<CompilationUnit> compilationUnits) {
        ProjectTree tree = new ProjectTree();
        compilationUnits.forEach(
                compilationUnit -> tree.getSourceCodeUnits().add(createSourceCodeUnit(compilationUnit)));
        return tree;
    }

    public SourceCodeUnit createSourceCodeUnit(CompilationUnit compilationUnit) {
        SourceCodeUnit codeUnit = new SourceCodeUnit(compilationUnit);
        codeUnit.calculateMetrics();
        List<ClassOrInterfaceUnit> classes = compilationUnit.getTypes().stream()
                .filter(node -> node instanceof ClassOrInterfaceDeclaration)
                .map(node -> new ClassOrInterfaceUnit((ClassOrInterfaceDeclaration) node))
                .collect(Collectors.toList());
        codeUnit.getClassOrInterfaceUnits().addAll(classes);

        List<EnumUnit> enums = compilationUnit.getTypes().stream()
                .filter(node -> node instanceof EnumDeclaration)
                .map(node -> new EnumUnit((EnumDeclaration) node))
                .collect(Collectors.toList());
        codeUnit.getEnumUnits().addAll(enums);

        classes.forEach(
                element -> element.getClassOrInterfaceDeclaration().getMembers().stream()
                        .filter(node -> node instanceof MethodDeclaration)
                        .map(node -> new MethodUnit((MethodDeclaration) node))
                        .forEach(methodUnit -> element.getMethodUnits().add(methodUnit))
        );

        classes.forEach(
                element -> element.getClassOrInterfaceDeclaration().getMembers().stream()
                        .filter(node -> node instanceof InitializerDeclaration)
                        .map(node -> new InitializerDeclarationUnit((InitializerDeclaration) node))
                        .forEach(initializerDeclarationUnit -> element.getInitializerDeclarationUnits().add(initializerDeclarationUnit))
        );


        codeUnit.calculateMetrics();
        return codeUnit;
    }
}
