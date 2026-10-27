package com.vuryss.aoc.cli;

import picocli.CommandLine;

public class DayConverter implements CommandLine.ITypeConverter<Integer> {
    @Override
    public Integer convert(String value) {
        final int day;

        try {
            day = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new CommandLine.TypeConversionException("Invalid puzzle day: " + value);
        }

        if (day < 1 || day > 25) {
            throw new CommandLine.TypeConversionException("Puzzle day must be between 1 and 25");
        }

        return day;
    }
}
