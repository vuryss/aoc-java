package com.vuryss.aoc.solutions;

import com.palantir.javapoet.*;
import com.vuryss.aoc.PuzzleDayReference;
import com.vuryss.aoc.client.AdventOfCodeClient;
import lombok.RequiredArgsConstructor;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

@RequiredArgsConstructor
public class SolutionScaffolding {
    private final AdventOfCodeClient adventOfCodeClient;
    private final Path sourceRoot;

    public void scaffold(PuzzleDayReference puzzle) {
        AnnotationSpec suppressUnused = AnnotationSpec.builder(SuppressWarnings.class)
            .addMember("value", "$S", "unused")
            .build();

        MethodSpec part1Tests = MethodSpec.methodBuilder("part1Tests")
            .addAnnotation(Override.class)
            .addModifiers(Modifier.PUBLIC)
            .returns(ParameterizedTypeName.get(Map.class, String.class, String.class))
            .addStatement("return Map.of()")
            .build();

        MethodSpec part2Tests = MethodSpec.methodBuilder("part2Tests")
            .addAnnotation(Override.class)
            .addModifiers(Modifier.PUBLIC)
            .returns(ParameterizedTypeName.get(Map.class, String.class, String.class))
            .addStatement("return Map.of()")
            .build();

        MethodSpec part1Solution = MethodSpec.methodBuilder("part1Solution")
            .addAnnotation(Override.class)
            .addModifiers(Modifier.PUBLIC)
            .returns(String.class)
            .addParameter(String.class, "input")
            .addParameter(boolean.class, "isTest")
            .addStatement("return \"\"")
            .build();

        MethodSpec part2Solution = MethodSpec.methodBuilder("part2Solution")
            .addAnnotation(Override.class)
            .addModifiers(Modifier.PUBLIC)
            .returns(String.class)
            .addParameter(String.class, "input")
            .addParameter(boolean.class, "isTest")
            .addStatement("return \"\"")
            .build();

        String description = adventOfCodeClient.getDayDescription(puzzle).block();
        assert description != null;
        description = description.replace("$", "$$");

        TypeSpec solution = TypeSpec.classBuilder("Day"+puzzle.day())
            .addJavadoc(description)
            .addAnnotation(suppressUnused)
            .addModifiers(Modifier.PUBLIC)
            .addSuperinterface(SolutionInterface.class)
            .addMethod(part1Tests)
            .addMethod(part2Tests)
            .addMethod(part1Solution)
            .addMethod(part2Solution)
            .build();

        var packageName = SolutionInterface.class.getPackageName();

        try {
            JavaFile
                .builder("%s.event%s".formatted(packageName, puzzle.event()), solution)
                .indent("    ")
                .build()
                .writeTo(sourceRoot);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
