package ru.rsreu.gorkin.codeanalyzer.core.adapters;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import ru.rsreu.gorkin.codeanalyzer.core.syntaxelements.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
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

        classes.forEach(
                element -> element.getClassOrInterfaceDeclaration().getMembers().stream()
                        .filter(node -> node instanceof FieldDeclaration)
                        .map(node -> new FieldUnit((FieldDeclaration) node))
                        .forEach(fieldUnit -> element.getFieldUnits().add(fieldUnit))
        );


        classes.forEach(
                element -> element.getClassOrInterfaceDeclaration().getMembers().stream()
                        .filter(node -> node instanceof ConstructorDeclaration)
                        .map(node -> new ConstructorUnit((ConstructorDeclaration) node))
                        .forEach(constructorUnit -> element.getConstructorUnits().add(constructorUnit))
        );


        codeUnit.calculateMetrics();
        return codeUnit;
    }

    public SourceCodeUnit createSourceCodeUnit(File file) throws FileNotFoundException {
        CompilationUnit cu = StaticJavaParser.parse(file);
        return createSourceCodeUnit(cu);
    }

    public ProjectTree create(File file) throws FileNotFoundException {
        CompilationUnit cu = StaticJavaParser.parse(file);
        List<CompilationUnit> compilationUnits = new ArrayList<>();
        compilationUnits.add(cu);
        return create(compilationUnits);
    }
}
