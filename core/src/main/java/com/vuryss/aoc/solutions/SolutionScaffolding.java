package com.vuryss.aoc.solutions;

import com.palantir.javapoet.*;
import com.vuryss.aoc.client.AdventOfCodeClient;
import jakarta.enterprise.context.ApplicationScoped;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;

@ApplicationScoped
public class SolutionScaffolding {
    AdventOfCodeClient adventOfCodeClient;

    public SolutionScaffolding(
        AdventOfCodeClient adventOfCodeClient
    ) {
        this.adventOfCodeClient = adventOfCodeClient;
    }

    public void scaffold(int year, int day) {
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

        TypeSpec solution = TypeSpec.classBuilder("Day"+day)
            .addJavadoc(adventOfCodeClient.getDayDescription(year, day))
            .addAnnotation(suppressUnused)
            .addModifiers(Modifier.PUBLIC)
            .addSuperinterface(SolutionInterface.class)
            .addMethod(part1Tests)
            .addMethod(part2Tests)
            .addMethod(part1Solution)
            .addMethod(part2Solution)
            .build();

        try {
            JavaFile
                .builder("com.vuryss.aoc.solutions.event"+year, solution)
                .indent("    ")
                .build()
                .writeTo(Paths.get("core/src/main/java"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
