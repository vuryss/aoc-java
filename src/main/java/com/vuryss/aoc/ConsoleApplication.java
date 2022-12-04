package com.vuryss.aoc;

import com.vuryss.aoc.service.Game;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Year;

@Component
public class ConsoleApplication implements ApplicationRunner {
    private final Game game;

    public ConsoleApplication(Game game) {
        this.game = game;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        var year = getYear(args);
        var day = getDay(args);
        var isTest = getIsTest(args);

        System.out.println("Solving for year " + year + " day " + day + (isTest ? " with test data" : ""));

        game.execute(year, day, isTest);
    }

    private int getYear(ApplicationArguments args) {
        final int firstYear = 2015;
        final int lastYear = Year.now().getValue();

        if (args.containsOption("year")) {
            var values = args.getOptionValues("year");

            if (values.size() > 1) {
                throw new RuntimeException("Can only specify single year");
            } else if (values.size() == 0) {
                throw new RuntimeException("You need to specify year for the challenges");
            }

            var givenYear = Integer.parseInt(values.get(0));

            if (givenYear >= firstYear && givenYear <= lastYear) {
                return givenYear;
            }

            throw new RuntimeException("Invalid year given");
        }

        // If we're not in December yet - fallback to previous year, challenges for the current one are not published
        if (LocalDate.now().getMonth().getValue() < 12) {
            return lastYear - 1;
        }

        // Use current year (we're in December - actively solving).
        return lastYear;
    }

    private int getDay(ApplicationArguments args) {
        if (!args.containsOption("day")) {
            throw new RuntimeException("A day argument is required to solve a challenge");
        }

        var values = args.getOptionValues("day");

        if (values.size() > 1) {
            throw new RuntimeException("Can only specify single day");
        } else if (values.size() == 0) {
            throw new RuntimeException("You need to specify day for the challenges");
        }

        var givenDay = Integer.parseInt(values.get(0));

        if (givenDay >= 1 && givenDay <= 25) {
            return givenDay;
        }

        throw new RuntimeException("Invalid day given");
    }

    private boolean getIsTest(ApplicationArguments args) {
        return args.containsOption("test");
    }
}
