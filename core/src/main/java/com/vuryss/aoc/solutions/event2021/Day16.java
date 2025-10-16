package com.vuryss.aoc.solutions.event2021;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.StringUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class Day16 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            D2FE28
            """,
            "6",
            """
            8A004A801A8002F478
            """,
            "16",
            """
            620080001611562C8802118E34
            """,
            "12",
            """
            C0015000016115A2E0802F182340
            """,
            "23",
            """
            A0016C880162017C3686B18A3D4780
            """,
            "31"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            C200B40A82
            """,
            "3",
            """
            04005AC33890
            """,
            "54",
            """
            880086C3E88112
            """,
            "7",
            """
            CE00C43D881120
            """,
            "9",
            """
            D8005AC2A8F0
            """,
            "1",
            """
            F600BC2D8F
            """,
            "0",
            """
            9C005AC2F8F0
            """,
            "0",
            """
            9C0141080250320F1802104A08
            """,
            "1"
        );
    }

    @Override
    public String part1Solution(String input, boolean isTest) {
        var binary = StringUtil.hex2bin(input);
        var packet = parse(binary);

        return String.valueOf(packet.sumVersions());
    }

    @Override
    public String part2Solution(String input, boolean isTest) {
        var binary = StringUtil.hex2bin(input);
        var packet = parse(binary);

        return String.valueOf(packet.getValue());
    }

    private Packet parse(String binary) {
        var version = Integer.parseInt(binary.substring(0, 3), 2);
        var type = Integer.parseInt(binary.substring(3, 6), 2);
        binary = binary.substring(6);
        var binaryLength = 6;

        if (type == 4) {
            var binaryNumber = new StringBuilder();

            while (binary.charAt(0) == '1') {
                binaryNumber.append(binary, 1, 5);
                binary = binary.substring(5);
                binaryLength += 5;
            }

            binaryNumber.append(binary, 1, 5);
            binaryLength += 5;

            return new Packet(
                version,
                type,
                new ArrayList<>(),
                Long.parseLong(binaryNumber.toString(), 2),
                binaryLength
            );
        }

        var lengthTypeID = binary.charAt(0);
        var subPackets = new ArrayList<Packet>();
        binary = binary.substring(1);
        binaryLength++;

        if (lengthTypeID == '0') {
            var length = Integer.parseInt(binary.substring(0, 15), 2);
            var usedLength = 0;
            binary = binary.substring(15);
            binaryLength += 15;

            while (usedLength < length) {
                var subPacket = parse(binary.substring(0, length - usedLength));
                subPackets.add(subPacket);
                usedLength += subPacket.binaryLength;
                binary = binary.substring(subPacket.binaryLength);
            }

            return new Packet(version, type, subPackets, 0, binaryLength + length);
        }

        var numberOfSubPackets = Integer.parseInt(binary.substring(0, 11), 2);
        binary = binary.substring(11);
        binaryLength += 11;

        for (int i = 0; i < numberOfSubPackets; i++) {
            var subPacket = parse(binary);
            subPackets.add(subPacket);
            binary = binary.substring(subPacket.binaryLength);
            binaryLength += subPacket.binaryLength;
        }

        return new Packet(version, type, subPackets, 0, binaryLength);
    }
}

class Packet {
    int version;
    int type;
    List<Packet> subPackets;
    long value;
    int binaryLength;

    public Packet(int version, int type, List<Packet> subPackets, long value, int binaryLength) {
        this.version = version;
        this.type = type;
        this.subPackets = subPackets;
        this.value = value;
        this.binaryLength = binaryLength;
    }

    public Integer sumVersions() {
        return version + subPackets.stream().map(Packet::sumVersions).reduce(0, Integer::sum);
    }

    public Long getValue() {
        return switch (type) {
            case 0 -> subPackets.stream().map(Packet::getValue).reduce(0L, Long::sum);
            case 1 -> subPackets.stream().map(Packet::getValue).reduce(1L, (a, b) -> a * b);
            case 2 -> subPackets.stream().map(Packet::getValue).min(Long::compareTo).orElse(0L);
            case 3 -> subPackets.stream().map(Packet::getValue).max(Long::compareTo).orElse(0L);
            case 4 -> value;
            case 5 -> subPackets.get(0).getValue() > subPackets.get(1).getValue() ? 1L : 0L;
            case 6 -> subPackets.get(0).getValue() < subPackets.get(1).getValue() ? 1L : 0L;
            case 7 -> subPackets.get(0).getValue().equals(subPackets.get(1).getValue()) ? 1L : 0L;
            default -> throw new RuntimeException("Unknown type: " + type);
        };
    }
}