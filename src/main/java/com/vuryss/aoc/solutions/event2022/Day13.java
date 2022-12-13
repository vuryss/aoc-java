package com.vuryss.aoc.solutions.event2022;

import com.vuryss.aoc.DayInterface;
import com.vuryss.aoc.Utils;

import java.util.*;
import java.util.stream.Collectors;

public class Day13 implements DayInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            [1,1,3,1,1]
            [1,1,5,1,1]
            
            [[1],[2,3,4]]
            [[1],4]
            
            [9]
            [[8,7,6]]
            
            [[4,4],4,4]
            [[4,4],4,4,4]
            
            [7,7,7,7]
            [7,7,7]
            
            []
            [3]
            
            [[[]]]
            [[]]
            
            [1,[2,[3,[4,[5,6,7]]]],8,9]
            [1,[2,[3,[4,[5,6,0]]]],8,9]
            """,
            "13"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            [1,1,3,1,1]
            [1,1,5,1,1]
            
            [[1],[2,3,4]]
            [[1],4]
            
            [9]
            [[8,7,6]]
            
            [[4,4],4,4]
            [[4,4],4,4,4]
            
            [7,7,7,7]
            [7,7,7]
            
            []
            [3]
            
            [[[]]]
            [[]]
            
            [1,[2,[3,[4,[5,6,7]]]],8,9]
            [1,[2,[3,[4,[5,6,0]]]],8,9]
            """,
            "140"
        );
    }

    @Override
    public String part1Solution(String input) {
        var index = 1;
        var sum = 0;

        for (var inputPart: input.split("\n\n")) {
            var tuple = inputPart.split("\n");
            var left = parseList(Utils.toCharacterQueue(tuple[0]));
            var right = parseList(Utils.toCharacterQueue(tuple[1]));

            if (left.isInRightOrderComparedTo(right) == 1) {
                sum += index;
            }

            index++;
        }

        return String.valueOf(sum);
    }

    @Override
    public String part2Solution(String input) {
        input += "\n[[2]]\n[[6]]";
        var itemList = new ArrayList<Item>();

        for (var inputPart: input.split("\n+")) {
            itemList.add(parseList(Utils.toCharacterQueue(inputPart)));
        }

        itemList.sort((a, b) -> b.isInRightOrderComparedTo(a));

        var index = 1;
        var product = 1;

        for (var item: itemList) {
            var stringRepresentation = item.toString();

            if (stringRepresentation.equals("[[2]]") || stringRepresentation.equals("[[6]]")) {
                product *= index;
            }

            index++;
        }

        return String.valueOf(product);
    }

    private Item parseList(Queue<Character> characters) {
        characters.poll();

        var list = new Item(new ArrayList<>());

        while (!characters.isEmpty()) {
            if (characters.peek().equals('[')) {
                list.list.add(parseList(characters));
                continue;
            }

            if (characters.peek().equals(']')) {
                break;
            }

            if (characters.peek().equals(',')) {
                characters.poll();
                continue;
            }

            StringBuilder numberString = new StringBuilder();

            while (!characters.isEmpty() && characters.peek() >= '0' && characters.peek() <= '9') {
                numberString.append(characters.poll());
            }

            list.list.add(new Item(Integer.parseInt(numberString.toString())));
        }

        characters.poll();

        return list;
    }

    enum ItemType { LIST, INTEGER }

    static class Item {
        public ItemType type;
        public List<Item> list;
        public int integer;

        public Item(List<Item> list) {
            this.type = ItemType.LIST;
            this.list = list;
        }

        public Item(int integer) {
            this.type = ItemType.INTEGER;
            this.integer = integer;
        }

        public int isInRightOrderComparedTo(Item item) {
            if (this.type == ItemType.LIST && item.type == ItemType.LIST) {
                var index = 0;

                while (true) {
                    if (this.list.size() > index && item.list.size() > index) {
                        var result = this.list.get(index).isInRightOrderComparedTo(item.list.get(index));

                        if (result != 0) {
                            return result;
                        }
                    } else if (this.list.size() == item.list.size()) {
                        return 0;
                    } else if (this.list.size() == index) {
                        return 1;
                    } else if (item.list.size() == index) {
                        return -1;
                    }

                    index++;
                }
            }

            if (this.type == ItemType.INTEGER && item.type == ItemType.INTEGER) {
                return Integer.compare(item.integer, this.integer);
            }

            Item left = this.type == ItemType.INTEGER ? new Item(List.of(this)) : this;
            Item right = item.type == ItemType.INTEGER ? new Item(List.of(item)) : item;

            return left.isInRightOrderComparedTo(right);
        }
        
        public String toString() {
            return type == ItemType.INTEGER
                ? String.valueOf(integer)
                : list.stream().map(Item::toString).collect(Collectors.joining(",", "[", "]"));
        }
    }
}
