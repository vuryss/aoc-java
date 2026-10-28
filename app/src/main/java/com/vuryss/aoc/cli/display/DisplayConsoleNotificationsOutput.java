package com.vuryss.aoc.cli.display;

import com.vuryss.aoc.Configuration;
import com.vuryss.aoc.cli.NotificationsOutputInterface;
import dev.tamboui.inline.InlineDisplay;
import io.github.kusoroadeolu.clique.Clique;
import io.github.kusoroadeolu.clique.configuration.BoxConfiguration;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

@RequiredArgsConstructor
public class DisplayConsoleNotificationsOutput implements NotificationsOutputInterface, AutoCloseable {
    private static final String blankNewLIne = "\u00A0\n\u00A0";
    private final InlineDisplay display;
    private final Scheduler renderScheduler;

    @Override
    public void error(String message) {
        var content = Clique.ink().red().bold().on(message);
        var config = BoxConfiguration.builder()
            .borderColor("red")
            .padding(5)
            .build();

        println(Clique.box(config).content(blankNewLIne + content + blankNewLIne).get());
    }

    @Override
    public void announce(String message) {
        var content = Clique.ink().blue().bold().on(message);
        var config = BoxConfiguration.builder()
            .borderColor("blue")
            .padding(5)
            .build();

        println(Clique.box(config).content(blankNewLIne + content + blankNewLIne).get());
    }

    @Override
    public void notice(String message) {
        var content = Clique.ink().yellow().bold().on(message);
        var config = BoxConfiguration.builder()
            .borderColor("yellow")
            .build();

        println(Clique.box(config).content(content).get());
    }

    @Override
    public void exception(Throwable e) {
        if (!Configuration.exceptionStacktraceEnabled()) return;

        println("\n" + ExceptionUtils.getStackTrace(e) + "\n");
    }

    private void println(String message) {
        renderScheduler.schedule(() -> message.lines().forEach(display::println));
    }

    @Override
    public void close() {
        Mono.empty().subscribeOn(renderScheduler).block();
    }
}
