package com.vuryss.aoc.cli;

import com.vuryss.aoc.Configuration;
import picocli.CommandLine;

import java.util.stream.Collectors;

public class EventConverter implements CommandLine.ITypeConverter<Integer> {
    @Override
    public Integer convert(String value) {
        final int event;

        try {
            event = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new CommandLine.TypeConversionException("Invalid event year: " + value);
        }

        var supportedEvents = Configuration.getSupportedEvents();

        if (supportedEvents.contains(event)) return event;

        throw new CommandLine.TypeConversionException(
            "Unsupported event year '%d'. Supported events are: %s".formatted(
                event,
                supportedEvents.stream().sorted().map(String::valueOf).collect(Collectors.joining(", "))
            )
        );
    }
}
