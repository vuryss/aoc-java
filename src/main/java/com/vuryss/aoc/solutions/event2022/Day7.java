package com.vuryss.aoc.solutions.event2022;

import com.vuryss.aoc.solutions.DayInterface;

import java.util.*;

@SuppressWarnings("unused")
public class Day7 implements DayInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            $ cd /
            $ ls
            dir a
            14848514 b.txt
            8504156 c.dat
            dir d
            $ cd a
            $ ls
            dir e
            29116 f
            2557 g
            62596 h.lst
            $ cd e
            $ ls
            584 i
            $ cd ..
            $ cd ..
            $ cd d
            $ ls
            4060174 j
            8033020 d.log
            5626152 d.ext
            7214296 k
            """,
            "95437"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            $ cd /
            $ ls
            dir a
            14848514 b.txt
            8504156 c.dat
            dir d
            $ cd a
            $ ls
            dir e
            29116 f
            2557 g
            62596 h.lst
            $ cd e
            $ ls
            584 i
            $ cd ..
            $ cd ..
            $ cd d
            $ ls
            4060174 j
            8033020 d.log
            5626152 d.ext
            7214296 k
            """,
            "24933642"
        );
    }

    @Override
    public String part1Solution(String input) {
        var root = constructFilesystem(input);
        var dirs = getAllDirectories(root);
        var totalSize = 0;

        for (var dir: dirs) {
            if (dir.getSize() <= 100000) {
                totalSize += dir.getSize();
            }
        }

        return String.valueOf(totalSize);
    }

    @Override
    public String part2Solution(String input) {
        var root = constructFilesystem(input);
        var neededSpace = 30000000 - (70000000 - root.getSize());
        var dirs = getAllDirectories(root);
        var sorted = new TreeMap<Integer, Entity>();

        for (var dir: dirs) {
            sorted.put(dir.getSize(), dir);
        }

        for (var entry: sorted.entrySet()) {
            if (entry.getKey() > neededSpace) {
                return String.valueOf(entry.getKey());
            }
        }

        return "-error-";
    }

    private Entity constructFilesystem(String input) {
        var lines = input.split("\n");
        var root = new Entity(EntityType.DIRECTORY, "/");
        var currentDirectory = root;

        for (var line: lines) {
            if (line.startsWith("$ cd ")) {
                var directoryName = line.substring(5);

                if (directoryName.equals("..")) {
                    currentDirectory = currentDirectory.parent;
                } else if (directoryName.equals("/")) {
                    currentDirectory = root;
                } else {
                    var existingDir = currentDirectory.getSubdirectory(directoryName);

                    if (existingDir == null) {
                        var newDirectory = new Entity(EntityType.DIRECTORY, directoryName);
                        currentDirectory.add(newDirectory);
                        currentDirectory = newDirectory;
                    } else {
                        currentDirectory = existingDir;
                    }
                }
            } else if (line.startsWith("$ ls")) {
                continue;
            } else if (line.startsWith("dir ")) {
                line = line.substring(4);

                var existingDir = currentDirectory.getSubdirectory(line);

                if (existingDir == null) {
                    var newDirectory = new Entity(EntityType.DIRECTORY, line);
                    currentDirectory.add(newDirectory);
                }
            } else {
                var parts = line.split(" ");
                var existingFile = currentDirectory.getFile(parts[1]);

                if (existingFile == null) {
                    var newFile = new Entity(EntityType.FILE, parts[1], Integer.parseInt(parts[0]));
                    currentDirectory.add(newFile);
                }
            }
        }

        return root;
    }

    private Set<Entity> getAllDirectories(Entity dir) {
        var set = new HashSet<Entity>();

        set.add(dir);

        for (var entity: dir.contents) {
            if (entity.type.equals(EntityType.DIRECTORY)) {
                set.addAll(getAllDirectories(entity));
            }
        }

        return set;
    }

    enum EntityType {
        DIRECTORY,
        FILE,
    }

    static class Entity {
        public EntityType type;
        public String name;
        public int size;
        public Entity parent = null;
        public List<Entity> contents = new ArrayList<>();

        public Entity(EntityType type, String name) {
            this.type = type;
            this.name = name;
        }

        public Entity(EntityType type, String name, int size) {
            this.type = type;
            this.name = name;
            this.size = size;
        }

        public Entity getSubdirectory(String name) {
            for (var entity: contents) {
                if (entity.type.equals(EntityType.DIRECTORY) && entity.name.equals(name)) {
                    return entity;
                }
            }

            return null;
        }

        public Entity getFile(String name) {
            for (var entity: contents) {
                if (entity.type.equals(EntityType.FILE) && entity.name.equals(name)) {
                    return entity;
                }
            }

            return null;
        }

        public void add(Entity entity) {
            entity.parent = this;
            contents.add(entity);
        }

        public int getSize() {
            if (type.equals(EntityType.FILE)) {
                return this.size;
            }

            int size = 0;

            for (var entity: contents) {
                size += entity.getSize();
            }

            return size;
        }
    }
}