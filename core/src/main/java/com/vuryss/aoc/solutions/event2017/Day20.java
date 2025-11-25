package com.vuryss.aoc.solutions.event2017;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.PointLong3D;
import com.vuryss.aoc.util.StringUtil;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.*;

@SuppressWarnings("unused")
public class Day20 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of();
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of();
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var particles = parseParticles(input);
        var minAcceleration = Long.MAX_VALUE;
        var minAccelerationId = -1;

        for (var particle: particles) {
            var pa = particle.acceleration;
            var acceleration = Math.abs(pa.x) + Math.abs(pa.y) + Math.abs(pa.z);

            if (acceleration < minAcceleration) {
                minAcceleration = acceleration;
                minAccelerationId = particle.id;
            }
        }

        return String.valueOf(minAccelerationId);
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var particles = parseParticles(input);
        var pCount = particles.length;

        for (var i = 0; i < 500; i++) {
            var collisions = new HashMap<PointLong3D, List<Integer>>();

            for (var particle: particles) {
                if (particle != null) {
                    collisions.computeIfAbsent(particle.position, k -> new LinkedList<>()).add(particle.id);
                    particle.tick();
                }
            }

            for (var entry: collisions.entrySet()) {
                if (entry.getValue().size() > 1) {
                    for (var id: entry.getValue()) {
                        particles[id] = null;
                    }
                }
            }
        }

        return String.valueOf((int) Arrays.stream(particles).filter(Objects::nonNull).count());
    }

    private Particle[] parseParticles(String input) {
        var lines = input.trim().split("\n");
        var particles = new Particle[lines.length];

        for (var i = 0; i < lines.length; i++) {
            var ints = StringUtil.sints(lines[i]);
            particles[i] = new Particle(
                i,
                new PointLong3D(ints.get(0), ints.get(1), ints.get(2)),
                new PointLong3D(ints.get(3), ints.get(4), ints.get(5)),
                new PointLong3D(ints.get(6), ints.get(7), ints.get(8))
            );
        }

        return particles;
    }

    private static class Particle {
        public int id;
        public PointLong3D position;
        public PointLong3D velocity;
        public PointLong3D acceleration;

        public Particle(int id, PointLong3D position, PointLong3D velocity, PointLong3D acceleration) {
            this.id = id;
            this.position = position;
            this.velocity = velocity;
            this.acceleration = acceleration;
        }

        public void tick() {
            velocity = velocity.add(acceleration);
            position = position.add(velocity);
        }
    }
}
