package com.vuryss.aoc.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public final class Dump {

    // Reusable ObjectMapper for pretty-printing to the console.
    // It's configured to be human-readable and supports Java 8+ time types.
    private static final ObjectMapper CONSOLE_MAPPER = new ObjectMapper()
        .enable(SerializationFeature.INDENT_OUTPUT)
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE)
        .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
        .registerModule(new JavaTimeModule());

    // Reusable ObjectMapper for compact, single-line output for logs.
    private static final ObjectMapper LOG_MAPPER = new ObjectMapper()
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .registerModule(new JavaTimeModule());

    /**
     * Dumps a human-readable, pretty-printed JSON representation of any object to the console.
     * Handles serialization errors gracefully.
     *
     * @param object The object to dump.
     */
    public static void toConsole(Object object) {
        try {
            String jsonOutput = CONSOLE_MAPPER.writeValueAsString(object);
            System.out.println(jsonOutput);
        } catch (JsonProcessingException e) {
            System.err.println("Error dumping object to console: " + e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    /**
     * Converts any object to a compact, single-line JSON string, suitable for logging.
     *
     * @param object The object to convert.
     * @return A compact JSON string, or an error message if serialization fails.
     */
    public static String asString(Object object) {
        try {
            return LOG_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            // In a logging context, we don't want to throw an exception.
            // Return a meaningful error string instead.
            return "";
        }
    }

    public static void dumpGrid(int[][] grid, int padding) {
        var minx = 0;
        var maxx = grid.length;
        var miny = 0;
        var maxy = grid[0].length;

        for (var y = miny; y < maxy; y++) {
            for (var x = minx; x < maxx; x++) {
                System.out.print(String.format("%" + padding + "d", grid[x][y]));
            }
            System.out.println();
        }
    }
}
