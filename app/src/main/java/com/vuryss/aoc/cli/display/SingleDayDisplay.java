package com.vuryss.aoc.cli.display;

import com.vuryss.aoc.PuzzleDayReference;
import dev.tamboui.buffer.Buffer;
import dev.tamboui.inline.InlineDisplay;
import dev.tamboui.layout.Rect;
import dev.tamboui.style.Color;
import dev.tamboui.style.Style;
import dev.tamboui.text.Line;
import dev.tamboui.text.Span;
import dev.tamboui.widgets.paragraph.Paragraph;
import dev.tamboui.widgets.spinner.Spinner;
import dev.tamboui.widgets.spinner.SpinnerState;
import dev.tamboui.widgets.spinner.SpinnerStyle;

import java.io.IOException;
import java.util.List;

/**
 * Display for executing single solution. For example: event 2023, day 10.
 */
public class SingleDayDisplay implements AutoCloseable {
    private final static int HEIGHT = 6;
    private final PuzzleDayReference puzzle;
    private final InlineDisplay display;
    private final boolean testMode;


    private final Spinner inputSpinner = spinner(Color.LIGHT_CYAN, SpinnerStyle.DOTS);
    private final Spinner computationSpinner = spinner(Color.LIGHT_YELLOW, SpinnerStyle.ARC);
    private final Spinner validationSpinner = spinner(Color.LIGHT_MAGENTA, SpinnerStyle.DOTS);

    private final SpinnerState part1Spinner = new SpinnerState();
    private final SpinnerState part2Spinner = new SpinnerState();

    public SingleDayDisplay(PuzzleDayReference puzzle, boolean testMode) throws IOException {
        this.puzzle = puzzle;
        this.display = InlineDisplay.create(HEIGHT);
        this.testMode = testMode;
    }

    public SingleDayDisplay(PuzzleDayReference puzzle) throws IOException {
        this(puzzle, false);
    }

    public InlineDisplay display() {
        return display;
    }

    public void render(DayState state) {
        display.render((area, buffer) -> {
            renderHeader(row(area, 1), buffer);

            var rail = Line.from(Span.raw("  │").gray().dim());
            Paragraph.from(rail).render(row(area, 2), buffer);
            Paragraph.from(rail).render(row(area, 4), buffer);

            renderPart(1, state.part1(), row(area, 3), buffer);
            renderPart(2, state.part2(), row(area, 5), buffer);
        });
    }

    public void renderTests(List<TestCaseState> tests) {
        display.render((area, buffer) -> {
            renderHeader(row(area, 1), buffer);

            Paragraph.from(
                Line.from(Span.raw("  │").gray().dim())
            ).render(row(area, 2), buffer);

            if (tests.isEmpty()) {
                Paragraph.from(
                    Line.from(
                        Span.raw("  No tests for this day.").gray()
                    )
                ).render(row(area, 3), buffer);
                return;
            }

            for (int i = 0; i < tests.size(); i++) {
                var test = tests.get(i);
                renderPart(
                    test.part(),
                    "Part %d test %d".formatted(test.part(), test.number()),
                    i == tests.size() - 1,
                    test.state(),
                    row(area, i + 3),
                    buffer
                );
            }
        }, Math.max(4, tests.size() + 3), -1, -1);
    }

    private void renderHeader(Rect area, Buffer buffer) {
        var line = Line.from(
            Span.raw("◆ ADVENT OF CODE  ").fg(Color.LIGHT_GREEN).bold(),
            Span.raw(String.valueOf(puzzle.event())).fg(Color.LIGHT_CYAN).bold(),
            Span.raw("  │  ").gray(),
            Span.raw("DAY ").bold(),
            Span.raw(String.valueOf(puzzle.day())).fg(Color.LIGHT_CYAN).bold()
        );

        Paragraph.from(line).render(area, buffer);
    }
    private void renderPart(int part, PartState state, Rect area, Buffer buffer) {
        renderPart(part, "Part " + part, 2 == part, state, area, buffer);
    }

    private void renderPart(
        int part,
        String label,
        boolean last,
        PartState state,
        Rect area,
        Buffer buffer
    ) {
        var prefix = Line.from(
            Span.raw(last ? "  └─ " : "  ├─ ").gray(),
            Span.raw(label + "  ").bold()
        );
        Paragraph.from(prefix).render(area, buffer);

        var contentArea = after(area, prefix.width());
        var spinnerState = part == 1 ? part1Spinner : part2Spinner;

        switch (state.status()) {
            case QUEUED -> renderQueued(contentArea, buffer);
            case LOADING_INPUT -> renderLoading(spinnerState, contentArea, buffer);
            case RUNNING -> renderRunning(state, spinnerState, contentArea, buffer);
            case COMPLETED -> renderCompleted(state, spinnerState, contentArea, buffer);
            case ERROR -> renderError(state, contentArea, buffer);
        }
    }


    private void renderQueued(Rect area, Buffer buffer) {
        Paragraph.from(Line.from(
            badge("○ QUEUED", Color.GRAY),
            Span.raw("  waiting to start").gray().dim()
        )).render(area, buffer);
    }

    private void renderLoading(SpinnerState spinnerState, Rect area, Buffer buffer) {
        renderActivity(
            inputSpinner,
            spinnerState,
            Line.from(
                badge("LOADING INPUT", Color.LIGHT_CYAN),
                Span.raw("  waiting for puzzle input…").gray()
            ),
            area,
            buffer
        );
    }

    private void renderRunning(
        PartState state,
        SpinnerState spinnerState,
        Rect area,
        Buffer buffer
    ) {
        renderActivity(
            computationSpinner,
            spinnerState,
            Line.from(
                badge("COMPUTING", Color.LIGHT_YELLOW),
                Span.raw("  " + elapsedText(state) + " elapsed").gray()
            ),
            area,
            buffer
        );
    }

    private void renderCompleted(
        PartState state,
        SpinnerState spinnerState,
        Rect area,
        Buffer buffer
    ) {
        var statusBadge = switch (state.validationStatus()) {
            case NOT_REQUESTED -> badge("◆ SOLVED", Color.LIGHT_CYAN);
            case QUEUED -> badge("○ CHECK QUEUED", Color.LIGHT_MAGENTA);
            case LOADING_EXPECTED_RESULT -> badge("CHECKING ANSWER", Color.LIGHT_MAGENTA);
            case VALID -> badge("✓ CORRECT", Color.LIGHT_GREEN);
            case INVALID -> badge("✗ WRONG ANSWER", Color.LIGHT_RED);
            case UNAVAILABLE -> badge("? NO EXPECTED ANSWER", Color.LIGHT_BLUE);
            case ERROR -> badge("! VALIDATION ERROR", Color.LIGHT_YELLOW);
        };

        var line = Line.from(
            statusBadge,
            Span.raw("  " + state.result()).fg(Color.LIGHT_CYAN).bold()
        );

        line = switch (state.validationStatus()) {
            case NOT_REQUESTED -> line.append(Span.raw("  · not validated").gray().dim());
            case VALID, INVALID -> {
                if (!testMode && state.validationStatus() == PartState.ValidationStatus.VALID) yield line;

                yield line
                    .append(Span.raw("  │  expected: ").gray())
                    .append(Span.raw(state.expectedResult()).fg(Color.LIGHT_YELLOW).bold());
            }
            case ERROR -> line
                .append(Span.raw("  │  ").gray())
                .append(Span.raw(state.error()).fg(Color.LIGHT_YELLOW));
            default -> line;
        };

        line = line.append(Span.raw("  │  " + elapsedText(state)).gray());

        if (state.validationStatus() == PartState.ValidationStatus.LOADING_EXPECTED_RESULT) {
            renderActivity(validationSpinner, spinnerState, line, area, buffer);
        } else {
            Paragraph.from(line).render(area, buffer);
        }
    }

    private void renderError(PartState state, Rect area, Buffer buffer) {
        var line = Line.from(
            badge("✗ EXECUTION ERROR", Color.LIGHT_RED),
            Span.raw("  " + state.error()).fg(Color.LIGHT_RED)
        );

        if (testMode && state.expectedResult() != null) {
            line = line
                .append(Span.raw("  │  expected: ").gray())
                .append(Span.raw(state.expectedResult()).fg(Color.LIGHT_YELLOW).bold());
        }

        // Input failures occur before computation has a start time.
        if (state.startedAtNs() >= 0) {
            line = line.append(Span.raw("  │  " + elapsedText(state)).gray());
        }

        if (state.result() != null && !state.result().isBlank()) {
            line = line.append(Span.raw("  │  " + state.result()).fg(Color.LIGHT_CYAN));
        }

        Paragraph.from(line).render(area, buffer);
    }

    private void renderActivity(
        Spinner spinner,
        SpinnerState spinnerState,
        Line line,
        Rect area,
        Buffer buffer
    ) {
        if (area.isEmpty()) return;

        spinnerState.advance();

        int spinnerWidth = Math.min(spinner.maxFrameWidth(), area.width());
        spinner.render(
            new Rect(area.x(), area.y(), spinnerWidth, 1),
            buffer,
            spinnerState
        );

        Paragraph.from(line).render(after(area, spinnerWidth + 1), buffer);
    }

    private static String elapsedText(PartState state) {
        double ms = state.elapsedMs();
        return ms < 1_000
            ? "%.1f ms".formatted(ms)
            : "%.2f s".formatted(ms / 1_000);
    }

    private static Span badge(String text, Color color) {
        return Span.styled(" " + text + " ", Style.EMPTY.fg(color).bold());
    }

    private static Rect row(Rect area, int row) {
        return new Rect(area.x(), area.y() + row, area.width(), 1);
    }

    private static Rect after(Rect area, int columns) {
        int offset = Math.clamp(columns, 0, area.width());
        return new Rect(area.x() + offset, area.y(), area.width() - offset, area.height());
    }

    private static Spinner spinner(Color color, SpinnerStyle style) {
        return Spinner.builder()
            .spinnerStyle(style)
            .style(Style.EMPTY.fg(color).bold())
            .build();
    }

    @Override
    public void close() throws Exception {
        display.close();
    }
}
