package com.vuryss.aoc.solutions.event2020;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.Direction;
import com.vuryss.aoc.util.MatrixUtil;
import com.vuryss.aoc.util.StringUtil;

import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.*;

@SuppressWarnings("unused")
public class Day20 implements SolutionInterface {
    private static final int[][] SEA_MONSTER = {
        {0, 18},

        {1, 0},
        {1, 5},
        {1, 6},
        {1, 11},
        {1, 12},
        {1, 17},
        {1, 18},
        {1, 19},

        {2, 1},
        {2, 4},
        {2, 7},
        {2, 10},
        {2, 13},
        {2, 16}
    };
    private static final int MONSTER_WIDTH = 20;
    private static final int MONSTER_HEIGHT = 3;

    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            Tile 2311:
            ..##.#..#.
            ##..#.....
            #...##..#.
            ####.#...#
            ##.##.###.
            ##...#.###
            .#.#.#..##
            ..#....#..
            ###...#.#.
            ..###..###
            
            Tile 1951:
            #.##...##.
            #.####...#
            .....#..##
            #...######
            .##.#....#
            .###.#####
            ###.##.##.
            .###....#.
            ..#.#..#.#
            #...##.#..
            
            Tile 1171:
            ####...##.
            #..##.#..#
            ##.#..#.#.
            .###.####.
            ..###.####
            .##....##.
            .#...####.
            #.##.####.
            ####..#...
            .....##...
            
            Tile 1427:
            ###.##.#..
            .#..#.##..
            .#.##.#..#
            #.#.#.##.#
            ....#...##
            ...##..##.
            ...#.#####
            .#.####.#.
            ..#..###.#
            ..##.#..#.
            
            Tile 1489:
            ##.#.#....
            ..##...#..
            .##..##...
            ..#...#...
            #####...#.
            #..#.#.#.#
            ...#.#.#..
            ##.#...##.
            ..##.##.##
            ###.##.#..
            
            Tile 2473:
            #....####.
            #..#.##...
            #.##..#...
            ######.#.#
            .#...#.#.#
            .#########
            .###.#..#.
            ########.#
            ##...##.#.
            ..###.#.#.
            
            Tile 2971:
            ..#.#....#
            #...###...
            #.#.###...
            ##.##..#..
            .#####..##
            .#..####.#
            #..#.#..#.
            ..####.###
            ..#.#.###.
            ...#.#.#.#
            
            Tile 2729:
            ...#.#.#.#
            ####.#....
            ..#.#.....
            ....#..#.#
            .##..##.#.
            .#.####...
            ####.#.#..
            ##.####...
            ##..#.##..
            #.##...##.
            
            Tile 3079:
            #.#.#####.
            .#..######
            ..#.......
            ######....
            ####.#..#.
            .#...#.##.
            #.#####.##
            ..#.###...
            ..#.......
            ..#.###...
            """,
            "20899048083289"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            Tile 2311:
            ..##.#..#.
            ##..#.....
            #...##..#.
            ####.#...#
            ##.##.###.
            ##...#.###
            .#.#.#..##
            ..#....#..
            ###...#.#.
            ..###..###
            
            Tile 1951:
            #.##...##.
            #.####...#
            .....#..##
            #...######
            .##.#....#
            .###.#####
            ###.##.##.
            .###....#.
            ..#.#..#.#
            #...##.#..
            
            Tile 1171:
            ####...##.
            #..##.#..#
            ##.#..#.#.
            .###.####.
            ..###.####
            .##....##.
            .#...####.
            #.##.####.
            ####..#...
            .....##...
            
            Tile 1427:
            ###.##.#..
            .#..#.##..
            .#.##.#..#
            #.#.#.##.#
            ....#...##
            ...##..##.
            ...#.#####
            .#.####.#.
            ..#..###.#
            ..##.#..#.
            
            Tile 1489:
            ##.#.#....
            ..##...#..
            .##..##...
            ..#...#...
            #####...#.
            #..#.#.#.#
            ...#.#.#..
            ##.#...##.
            ..##.##.##
            ###.##.#..
            
            Tile 2473:
            #....####.
            #..#.##...
            #.##..#...
            ######.#.#
            .#...#.#.#
            .#########
            .###.#..#.
            ########.#
            ##...##.#.
            ..###.#.#.
            
            Tile 2971:
            ..#.#....#
            #...###...
            #.#.###...
            ##.##..#..
            .#####..##
            .#..####.#
            #..#.#..#.
            ..####.###
            ..#.#.###.
            ...#.#.#.#
            
            Tile 2729:
            ...#.#.#.#
            ####.#....
            ..#.#.....
            ....#..#.#
            .##..##.#.
            .#.####...
            ####.#.#..
            ##.####...
            ##..#.##..
            #.##...##.
            
            Tile 3079:
            #.#.#####.
            .#..######
            ..#.......
            ######....
            ####.#..#.
            .#...#.##.
            #.#####.##
            ..#.###...
            ..#.......
            ..#.###...
            """,
            "273"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var tiles = parseTiles(input);
        matchTiles(tiles);

        return tiles.stream()
            .filter(t -> t.adjacent.size() == 2)
            .mapToLong(t -> t.id)
            .reduce(1, (a, b) -> a * b) + "";
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var tiles = parseTiles(input);
        matchTiles(tiles);
        orientTiles(tiles);
        tiles.forEach(Tile::stripBorders);

        var topLeft = tiles.stream()
            .filter(t -> t.adjacent.get(Direction.U) == null && t.adjacent.get(Direction.L) == null)
            .findFirst().orElseThrow();
        var tilesPerSide = (int) Math.sqrt(tiles.size());
        var image = buildImage(topLeft, tilesPerSide);

        return roughness(image) + "";
    }

    private List<Tile> parseTiles(String input) {
        var list = new ArrayList<Tile>();
        var parts = input.trim().split("\n\n");

        for (var part: parts) {
            var id = StringUtil.ints(part).getFirst();
            part = part.substring(part.indexOf("\n") + 1);
            list.add(new Tile(id, StringUtil.toCharGrid(part)));
        }

        return list;
    }

    private void matchTiles(List<Tile> tiles) {
        for (int i = 0; i < tiles.size() - 1; i++) {
            nextTile:
            for (var j = i + 1; j < tiles.size(); j++) {
                var tile1 = tiles.get(i);
                var tile2 = tiles.get(j);

                for (var side1: Direction.values()) {
                    if (tile1.adjacent.containsKey(side1)) continue;

                    for (var side2: Direction.values()) {
                        if (tile2.adjacent.containsKey(side2)) continue;

                        if (edgesMatch(tile1.edge(side1), tile2.edge(side2))) {
                            tile1.adjacent.put(side1, tile2);
                            tile2.adjacent.put(side2, tile1);
                            continue nextTile;
                        }
                    }
                }
            }
        }
    }

    private boolean edgesMatch(String a, String b) {
        return a.equals(b) || a.contentEquals(new StringBuilder(b).reverse());
    }

    private void orientTiles(List<Tile> tiles) {
        var queue = new ArrayDeque<Tile>();
        var processed = new HashSet<Integer>();
        queue.addLast(tiles.getFirst());
        processed.add(tiles.getFirst().id);

        while (!queue.isEmpty()) {
            var tile = queue.removeFirst();

            for (var es: tile.adjacent.entrySet()) {
                var side = es.getKey();
                var neighbour = es.getValue();

                if (neighbour == null || processed.contains(neighbour.id)) continue;

                orientNeighbourTile(tile, neighbour, side.opposite(), tile.edge(side));
                queue.addLast(neighbour);
                processed.add(neighbour.id);
            }
        }
    }

    private void orientNeighbourTile(Tile tile, Tile neighbour, Direction side, String expectedEdge) {
        for (var flip = 0; flip < 2; flip++) {
            for (var rotate = 0; rotate < 4; rotate++) {
                if (neighbour.adjacent.get(side) == tile && neighbour.edge(side).equals(expectedEdge)) return;
                neighbour.rotateClockwise();
            }
            neighbour.flipHorizontally();
        }
    }

    private char[][] buildImage(Tile topLeft, int tilesPerSide) {
        int tileSize = topLeft.matrix.length;
        var image = new char[tilesPerSide * tileSize][tilesPerSide * tileSize];
        Tile rowStart = topLeft;

        for (var tileY = 0; tileY < tilesPerSide; tileY++) {
            var tile = rowStart;

            for (var tileX = 0; tileX < tilesPerSide; tileX++) {
                for (var y = 0; y < tileSize; y++) {
                    System.arraycopy(tile.matrix[y], 0, image[tileY * tileSize + y], tileX * tileSize, tileSize);
                }
                tile = tile.adjacent.get(Direction.R);
            }

            rowStart = rowStart.adjacent.get(Direction.D);
        }

        return image;
    }

    private int roughness(char[][] image) {
        var totalCount = 0;

        for (var row: image) for (var c: row) if (c == '#') totalCount++;

        for (var flip = 0; flip < 2; flip++) {
            for (var rotate = 0; rotate < 4; rotate++) {
                var monsters = countMonsters(image);

                if (monsters > 0) return totalCount - monsters * SEA_MONSTER.length;

                image = MatrixUtil.rotateClockwise(image);
            }
            image = MatrixUtil.flipHorizontally(image);
        }

        throw new RuntimeException("No monsters found");
    }

    private int countMonsters(char[][] image) {
        var count = 0;

        for (var y = 0; y <= image.length - MONSTER_HEIGHT; y++) {
            next:
            for (var x = 0; x <= image[y].length - MONSTER_WIDTH; x++) {
                for (var delta: SEA_MONSTER) {
                    if (image[y+delta[0]][x+delta[1]] != '#') continue next;
                }
                count++;
            }
        }

        return count;
    }

    private static class Tile {
        public int id;
        char[][] matrix;
        public Map<Direction, Tile> adjacent = new EnumMap<>(Direction.class);

        public Tile(int id, char[][] matrix) {
            this.id = id;
            this.matrix = matrix;
        }

        String edge(Direction side) {
            return switch (side) {
                case U -> new String(matrix[0]);
                case D -> new String(matrix[matrix.length - 1]);
                case L -> column(0);
                case R -> column(matrix[0].length - 1);
            };
        }

        private String column(int x) {
            var result = new StringBuilder();
            for (var row: matrix) result.append(row[x]);
            return result.toString();
        }

        public void rotateClockwise() {
            matrix = MatrixUtil.rotateClockwise(matrix);

            var newAdjacent = new HashMap<Direction, Tile>();
            newAdjacent.put(Direction.U, adjacent.get(Direction.L));
            newAdjacent.put(Direction.R, adjacent.get(Direction.U));
            newAdjacent.put(Direction.D, adjacent.get(Direction.R));
            newAdjacent.put(Direction.L, adjacent.get(Direction.D));

            adjacent = newAdjacent;
        }

        public void flipHorizontally() {
            matrix = MatrixUtil.flipHorizontally(matrix);

            var newAdjacent = new HashMap<Direction, Tile>();
            newAdjacent.put(Direction.U, adjacent.get(Direction.U));
            newAdjacent.put(Direction.R, adjacent.get(Direction.L));
            newAdjacent.put(Direction.D, adjacent.get(Direction.D));
            newAdjacent.put(Direction.L, adjacent.get(Direction.R));

            adjacent = newAdjacent;
        }

        public void stripBorders() {
            var newMatrix = new char[matrix.length - 2][matrix[0].length - 2];

            for (var i = 1; i < matrix.length - 1; i++) {
                for (var j = 1; j < matrix[0].length - 1; j++) {
                    newMatrix[i - 1][j - 1] = matrix[i][j];
                }
            }

            matrix = newMatrix;
        }
    }
}
