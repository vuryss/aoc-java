package com.vuryss.aoc.cli.display;

import com.vuryss.aoc.Configuration;
import dev.tamboui.inline.InlineDisplay;
import dev.tamboui.layout.Rect;
import dev.tamboui.style.Color;
import dev.tamboui.style.Style;
import dev.tamboui.text.Line;
import dev.tamboui.text.Span;
import dev.tamboui.widgets.paragraph.Paragraph;
import dev.tamboui.widgets.spinner.SpinnerStyle;

import java.io.IOException;

/**
 * Display for executing one or more full events of Advent of Code.
 * For example: events 2023 and 2024 - all solutions for each day of the event.
 */
public class WholeEventDisplay implements AutoCloseable {
    private final int event;
    private final boolean testMode;
    private final InlineDisplay display;
    private int spinnerFrame;

    public WholeEventDisplay(int event, boolean testMode) throws IOException {
        this.event = event;
        this.testMode = testMode;
        this.display = InlineDisplay.create(Configuration.getMaxChallengesForYear(event) + 3);
    }

    public InlineDisplay display() {
        return display;
    }

    public void render(EventState state) {
        display.render((area, buffer) -> {
            spinnerFrame++;
            var header = Line.from(Span.raw(
                "◆ ADVENT OF CODE %d  │  %s".formatted(event, testMode ? "TESTS" : "SOLUTIONS")
            )).fg(Color.LIGHT_GREEN).bold();
            Paragraph.from(header).render(row(area, 0), buffer);

            int labelWidth = Math.min(9, area.width());
            int availableWidth = area.width() - labelWidth;
            int partWidth = availableWidth / 2;

            for (int index = 0; index < state.days().size(); index++) {
                var day = state.days().get(index);
                var dayArea = row(area, index + 2);
                var label = Line.from(Span.raw("Day %02d".formatted(index + 1)).bold());
                
                Paragraph.from(label).render(slice(dayArea, 0, labelWidth), buffer);
                Paragraph.from(
                    partLine(1, day.part1())
                ).render(slice(dayArea, labelWidth, partWidth), buffer);
                Paragraph.from(
                    partLine(2, day.part2())
                ).render(slice(dayArea, labelWidth + partWidth, availableWidth - partWidth), buffer);
            }

            var footer = Line.from(Span.raw(
                "Finished %d/%d days".formatted(state.finishedDays(), state.days().size())
            ).gray());
            Paragraph.from(footer).render(row(area, state.days().size() + 2), buffer);
        });
    }

    private Line partLine(int part, PartState state) {
        var statusColor = partColor(state);
        var result = state.result();

        // Keep the result position visible before a result is available.
        if (result == null || result.isBlank()) {
            result = switch (state.status()) {
                case LOADING_INPUT -> SpinnerStyle.DOTS.frame(spinnerFrame);
                case RUNNING -> SpinnerStyle.ARC.frame(spinnerFrame);
                default -> "—";
            };
        }

        // Once computation finishes, validation activity belongs beside the status.
        var statusActivity =
            state.status() == PartState.Status.COMPLETED && state.validationStatus() == PartState.ValidationStatus.LOADING_EXPECTED_RESULT
                ? SpinnerStyle.DOTS.frame(spinnerFrame) + " "
                : "";

        var errorDetails =
            state.status() == PartState.Status.ERROR && state.error() != null && !state.error().isBlank()
                ? " " + state.error()
                : "";

        return Line.from(
            Span.raw("Part " + part + ": ").bold(),

            Span.raw(result).fg(Color.LIGHT_CYAN).bold(),

            Span.raw(" (" + elapsedText(state) + ") ").gray(),

            Span.styled(statusActivity + partText(state), Style.EMPTY.fg(statusColor).bold()),

            Span.raw(errorDetails + "\u00A0").fg(statusColor).bg(Color.RESET)
        );
    }

    private String partText(PartState state) {
        return switch (state.status()) {
            case QUEUED -> "○ QUEUED";
            case LOADING_INPUT -> "LOADING INPUT";
            case RUNNING -> testMode ? "TESTING" : "COMPUTING";
            case ERROR -> "✗ ERROR";
            case COMPLETED -> switch (state.validationStatus()) {
                case NOT_REQUESTED -> "◆ SOLVED";

                case QUEUED -> testMode
                    ? "◆ COMPUTED · TESTS QUEUED"
                    : "◆ COMPUTED · ANSWERS QUEUED";

                case LOADING_EXPECTED_RESULT -> testMode
                    ? "◆ COMPUTED · LOADING TESTS"
                    : "◆ COMPUTED · DOWNLOADING ANSWERS";

                case VALID -> testMode ? "✓ PASSED" : "✓ CORRECT";
                case INVALID -> (testMode ? "✗ FAILED" : "✗ WRONG") + " (expected: " + state.expectedResult() + ")";
                case UNAVAILABLE -> testMode ? "? NO TESTS" : "? NO ANSWER";
                case ERROR -> testMode ? "! TEST ERROR" : "! VALIDATION ERROR";
            };
        };
    }
    
    private Color partColor(PartState state) {
        return switch (state.status()) {
            case QUEUED -> Color.GRAY;
            case LOADING_INPUT -> Color.LIGHT_CYAN;
            case RUNNING -> Color.LIGHT_YELLOW;
            case ERROR -> Color.LIGHT_RED;
            case COMPLETED -> switch (state.validationStatus()) {
                case NOT_REQUESTED -> Color.LIGHT_CYAN;
                case VALID -> Color.LIGHT_GREEN;
                case INVALID -> Color.LIGHT_RED;
                case UNAVAILABLE -> Color.GRAY;
                case ERROR -> testMode ? Color.LIGHT_RED : Color.LIGHT_YELLOW;
                case QUEUED, LOADING_EXPECTED_RESULT -> Color.LIGHT_MAGENTA;
            };
        };
    }

    private static String elapsedText(PartState state) {
        boolean isRunning = state.status() == PartState.Status.RUNNING;

        if (state.startedAtNs() < 0 || (!isRunning && state.finishedAtNs() < 0)) return "—";

        double ms = state.elapsedMs();
        return ms < 1_000 ? "%.1f ms".formatted(ms) : "%.2f s".formatted(ms / 1_000);
    }

    /**
     * Select one horizontal row within the display.
     */
    private static Rect row(Rect area, int index) {
        return new Rect(area.x(), area.y() + index, area.width(), 1);
    }

    /**
     * Select a column segment within a row.
     */
    private static Rect slice(Rect area, int offset, int width) {
        return new Rect(area.x() + offset, area.y(), width, 1);
    }

    @Override
    public void close() throws Exception {
        display.close();
    }
}
