package com.vuryss.aoc.solutions.event2022;

import com.vuryss.aoc.DayInterface;

import java.util.*;

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
        var sorted = new TreeMap<Integer, Dir>();

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

    private Dir constructFilesystem(String input) {
        var lines = input.split("\n");
        var root = new Dir("/");
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
                        var newDirectory = new Dir(directoryName);
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
                    var newDirectory = new Dir(line);
                    currentDirectory.add(newDirectory);
                }
            } else {
                var parts = line.split(" ");
                var existingFile = currentDirectory.getFile(parts[1]);

                if (existingFile == null) {
                    var newFile = new File(parts[1], Integer.parseInt(parts[0]));
                    currentDirectory.add(newFile);
                }
            }
        }

        return root;
    }

    private Set<Dir> getAllDirectories(Dir dir) {
        var set = new HashSet<Dir>();

        set.add(dir);

        for (var entity: dir.contents) {
            if (entity instanceof Dir) {
                set.addAll(getAllDirectories((Dir) entity));
            }
        }

        return set;
    }
}

interface Entity {
    void setParent(Dir parent);
    int getSize();
}

class Dir implements Entity {
    public String name;
    public Dir parent = null;
    public List<Entity> contents = new ArrayList<>();

    public Dir(String name) {
        this.name = name;
    }

    public Dir getSubdirectory(String name) {
        for (var entity: contents) {
            if (entity instanceof Dir && ((Dir) entity).name.equals(name)) {
                return (Dir) entity;
            }
        }

        return null;
    }

    public File getFile(String name) {
        for (var entity: contents) {
            if (entity instanceof File && ((File) entity).name.equals(name)) {
                return (File) entity;
            }
        }

        return null;
    }

    public void add(Entity entity) {
        entity.setParent(this);
        contents.add(entity);
    }

    @Override
    public void setParent(Dir parent) {
        this.parent = parent;
    }

    public int getSize() {
        int size = 0;

        for (var entity: contents) {
            size += entity.getSize();
        }

        return size;
    }
}

class File implements Entity {
    public String name;
    public Dir parent;
    public int size;

    public File(String name, int size) {
        this.name = name;
        this.size = size;
    }

    @Override
    public void setParent(Dir parent) {
        this.parent = parent;
    }

    @Override
    public int getSize() {
        return size;
    }
}