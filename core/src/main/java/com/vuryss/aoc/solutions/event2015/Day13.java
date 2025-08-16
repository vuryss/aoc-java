package com.vuryss.aoc.solutions.event2015;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.Regex;

import java.util.*;

@SuppressWarnings("unused")
public class Day13 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            Alice would gain 54 happiness units by sitting next to Bob.
            Alice would lose 79 happiness units by sitting next to Carol.
            Alice would lose 2 happiness units by sitting next to David.
            Bob would gain 83 happiness units by sitting next to Alice.
            Bob would lose 7 happiness units by sitting next to Carol.
            Bob would lose 63 happiness units by sitting next to David.
            Carol would lose 62 happiness units by sitting next to Alice.
            Carol would gain 60 happiness units by sitting next to Bob.
            Carol would gain 55 happiness units by sitting next to David.
            David would gain 46 happiness units by sitting next to Alice.
            David would lose 7 happiness units by sitting next to Bob.
            David would gain 41 happiness units by sitting next to Carol.
            """,
            "330"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            Alice would gain 54 happiness units by sitting next to Bob.
            Alice would lose 79 happiness units by sitting next to Carol.
            Alice would lose 2 happiness units by sitting next to David.
            Bob would gain 83 happiness units by sitting next to Alice.
            Bob would lose 7 happiness units by sitting next to Carol.
            Bob would lose 63 happiness units by sitting next to David.
            Carol would lose 62 happiness units by sitting next to Alice.
            Carol would gain 60 happiness units by sitting next to Bob.
            Carol would gain 55 happiness units by sitting next to David.
            David would gain 46 happiness units by sitting next to Alice.
            David would lose 7 happiness units by sitting next to Bob.
            David would gain 41 happiness units by sitting next to Carol.
            """,
            "286"
        );
    }

    @Override
    public String part1Solution(String input) {
        var people = parsePeople(input);
        var maxHappiness = calculateMaxHappiness(people);

        return String.valueOf(maxHappiness);
    }

    @Override
    public String part2Solution(String input) {
        var people = parsePeople(input);
        var me = new Person("me");

        for (var person: people.values()) {
            person.happiness.put(me, 0);
            me.happiness.put(person, 0);
        }

        people.put(me.name, me);

        var maxHappiness = calculateMaxHappiness(people);

        return String.valueOf(maxHappiness);
    }

    private HashMap<String, Person> parsePeople(String input) {
        var lines = input.trim().split("\n");
        var people = new HashMap<String, Person>();

        for (var line: lines) {
            var parts = Regex.matchGroups("^(\\w+) would (gain|lose) (\\d+) happiness units by sitting next to (\\w+)", line);
            assert parts != null;
            var person = people.computeIfAbsent(parts.getFirst(), Person::new);

            person.happiness.put(
                people.computeIfAbsent(parts.get(3), Person::new),
                Integer.parseInt(parts.get(2)) * (parts.get(1).equals("gain") ? 1 : -1)
            );
        }

        return people;
    }

    private int calculateMaxHappiness(HashMap<String, Person> people) {
        var startPerson = people.get(people.keySet().iterator().next());
        var maxHappiness = 0;
        var queue = new LinkedList<State>();

        queue.add(new State(startPerson, 0, Set.of(startPerson)));

        while (!queue.isEmpty()) {
            var state = queue.poll();
            var person = state.person;
            var nextPeople = person.happiness.keySet().stream().filter(p -> !state.visited.contains(p)).toList();
            var happiness = state.happiness;

            if (nextPeople.isEmpty()) {
                happiness += person.happiness.get(startPerson) + startPerson.happiness.get(person);
                maxHappiness = Math.max(maxHappiness, happiness);
                continue;
            }

            for (var nextPerson: nextPeople) {
                var newVisited = new HashSet<>(state.visited);
                newVisited.add(nextPerson);

                queue.add(new State(
                    nextPerson,
                    happiness + person.happiness.get(nextPerson) + nextPerson.happiness.get(person),
                    newVisited
                ));
            }
        }

        return maxHappiness;
    }

    static class Person {
        public String name;
        public Map<Person, Integer> happiness = new HashMap<>();

        public Person(String name) {
            this.name = name;
        }

        public String toString() {
            return "Person[name=" + name + "]";
        }
    }

    record State(Person person, int happiness, Set<Person> visited) {}
}


