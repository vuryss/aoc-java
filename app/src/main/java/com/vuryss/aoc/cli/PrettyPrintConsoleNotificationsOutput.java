package com.vuryss.aoc.cli;

import com.vuryss.aoc.Configuration;
import io.github.kusoroadeolu.clique.Clique;
import io.github.kusoroadeolu.clique.configuration.BoxConfiguration;

public class PrettyPrintConsoleNotificationsOutput implements NotificationsOutputInterface {
    private static final String blankNewLIne = "\u00A0\n\u00A0";

    @Override
    public void error(String message) {
        var content = Clique.ink().red().bold().on(message);
        var config = BoxConfiguration.builder()
            .borderColor("red")
            .padding(5)
            .build();

        Clique.box(config)
            .content(blankNewLIne + content + blankNewLIne)
            .render();
    }

    @Override
    public void announce(String message) {
        var content = Clique.ink().blue().bold().on(message);
        var config = BoxConfiguration.builder()
            .borderColor("blue")
            .padding(5)
            .build();

        Clique.box(config)
            .content(blankNewLIne + content + blankNewLIne)
            .render();
    }

    @Override
    public void notice(String message) {
        var content = Clique.ink().yellow().bold().on(message);
        var config = BoxConfiguration.builder()
            .borderColor("yellow")
            .padding(3)
            .build();

        Clique.box(config)
            .content(content)
            .render();
    }

    @Override
    public void exception(Throwable e) {
        if (!Configuration.exceptionStacktraceEnabled()) return;

        System.err.println();
        e.printStackTrace(System.err);
        System.err.println();
    }
}
