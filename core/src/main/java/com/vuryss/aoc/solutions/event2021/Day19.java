package com.vuryss.aoc.solutions.event2021;

import com.vuryss.aoc.solutions.SolutionInterface;
import com.vuryss.aoc.util.Point3D;
import com.vuryss.aoc.util.StringUtil;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class Day19 implements SolutionInterface {
    @Override
    public Map<String, String> part1Tests() {
        return Map.of(
            """
            --- scanner 0 ---
            404,-588,-901
            528,-643,409
            -838,591,734
            390,-675,-793
            -537,-823,-458
            -485,-357,347
            -345,-311,381
            -661,-816,-575
            -876,649,763
            -618,-824,-621
            553,345,-567
            474,580,667
            -447,-329,318
            -584,868,-557
            544,-627,-890
            564,392,-477
            455,729,728
            -892,524,684
            -689,845,-530
            423,-701,434
            7,-33,-71
            630,319,-379
            443,580,662
            -789,900,-551
            459,-707,401
            
            --- scanner 1 ---
            686,422,578
            605,423,415
            515,917,-361
            -336,658,858
            95,138,22
            -476,619,847
            -340,-569,-846
            567,-361,727
            -460,603,-452
            669,-402,600
            729,430,532
            -500,-761,534
            -322,571,750
            -466,-666,-811
            -429,-592,574
            -355,545,-477
            703,-491,-529
            -328,-685,520
            413,935,-424
            -391,539,-444
            586,-435,557
            -364,-763,-893
            807,-499,-711
            755,-354,-619
            553,889,-390
            
            --- scanner 2 ---
            649,640,665
            682,-795,504
            -784,533,-524
            -644,584,-595
            -588,-843,648
            -30,6,44
            -674,560,763
            500,723,-460
            609,671,-379
            -555,-800,653
            -675,-892,-343
            697,-426,-610
            578,704,681
            493,664,-388
            -671,-858,530
            -667,343,800
            571,-461,-707
            -138,-166,112
            -889,563,-600
            646,-828,498
            640,759,510
            -630,509,768
            -681,-892,-333
            673,-379,-804
            -742,-814,-386
            577,-820,562
            
            --- scanner 3 ---
            -589,542,597
            605,-692,669
            -500,565,-823
            -660,373,557
            -458,-679,-417
            -488,449,543
            -626,468,-788
            338,-750,-386
            528,-832,-391
            562,-778,733
            -938,-730,414
            543,643,-506
            -524,371,-870
            407,773,750
            -104,29,83
            378,-903,-323
            -778,-728,485
            426,699,580
            -438,-605,-362
            -469,-447,-387
            509,732,623
            647,635,-688
            -868,-804,481
            614,-800,639
            595,780,-596
            
            --- scanner 4 ---
            727,592,562
            -293,-554,779
            441,611,-461
            -714,465,-776
            -743,427,-804
            -660,-479,-426
            832,-632,460
            927,-485,-438
            408,393,-506
            466,436,-512
            110,16,151
            -258,-428,682
            -393,719,612
            -211,-452,876
            808,-476,-593
            -575,615,604
            -485,667,467
            -680,325,-822
            -627,-443,-432
            872,-547,-609
            833,512,582
            807,604,487
            839,-516,451
            891,-625,532
            -652,-548,-490
            30,-46,-14
            """,
            "79"
        );
    }

    @Override
    public Map<String, String> part2Tests() {
        return Map.of(
            """
            --- scanner 0 ---
            404,-588,-901
            528,-643,409
            -838,591,734
            390,-675,-793
            -537,-823,-458
            -485,-357,347
            -345,-311,381
            -661,-816,-575
            -876,649,763
            -618,-824,-621
            553,345,-567
            474,580,667
            -447,-329,318
            -584,868,-557
            544,-627,-890
            564,392,-477
            455,729,728
            -892,524,684
            -689,845,-530
            423,-701,434
            7,-33,-71
            630,319,-379
            443,580,662
            -789,900,-551
            459,-707,401
            
            --- scanner 1 ---
            686,422,578
            605,423,415
            515,917,-361
            -336,658,858
            95,138,22
            -476,619,847
            -340,-569,-846
            567,-361,727
            -460,603,-452
            669,-402,600
            729,430,532
            -500,-761,534
            -322,571,750
            -466,-666,-811
            -429,-592,574
            -355,545,-477
            703,-491,-529
            -328,-685,520
            413,935,-424
            -391,539,-444
            586,-435,557
            -364,-763,-893
            807,-499,-711
            755,-354,-619
            553,889,-390
            
            --- scanner 2 ---
            649,640,665
            682,-795,504
            -784,533,-524
            -644,584,-595
            -588,-843,648
            -30,6,44
            -674,560,763
            500,723,-460
            609,671,-379
            -555,-800,653
            -675,-892,-343
            697,-426,-610
            578,704,681
            493,664,-388
            -671,-858,530
            -667,343,800
            571,-461,-707
            -138,-166,112
            -889,563,-600
            646,-828,498
            640,759,510
            -630,509,768
            -681,-892,-333
            673,-379,-804
            -742,-814,-386
            577,-820,562
            
            --- scanner 3 ---
            -589,542,597
            605,-692,669
            -500,565,-823
            -660,373,557
            -458,-679,-417
            -488,449,543
            -626,468,-788
            338,-750,-386
            528,-832,-391
            562,-778,733
            -938,-730,414
            543,643,-506
            -524,371,-870
            407,773,750
            -104,29,83
            378,-903,-323
            -778,-728,485
            426,699,580
            -438,-605,-362
            -469,-447,-387
            509,732,623
            647,635,-688
            -868,-804,481
            614,-800,639
            595,780,-596
            
            --- scanner 4 ---
            727,592,562
            -293,-554,779
            441,611,-461
            -714,465,-776
            -743,427,-804
            -660,-479,-426
            832,-632,460
            927,-485,-438
            408,393,-506
            466,436,-512
            110,16,151
            -258,-428,682
            -393,719,612
            -211,-452,876
            808,-476,-593
            -575,615,604
            -485,667,467
            -680,325,-822
            -627,-443,-432
            872,-547,-609
            833,512,582
            807,604,487
            839,-516,451
            891,-625,532
            -652,-548,-490
            30,-46,-14
            """,
            "3621"
        );
    }

    @Override
    public String part1Solution(String input) {
        var scanners = processScanners(input);
        var uniquePoints = new HashSet<Point3D>();

        for (var scanner : scanners) {
            for (var beacon : scanner.beacons.values()) {
                uniquePoints.add(beacon.position);
            }
        }

        return String.valueOf(uniquePoints.size());
    }

    @Override
    public String part2Solution(String input) {
        var scanners = processScanners(input);
        var max = 0L;

        for (var scanner : scanners) {
            for (var scanner2 : scanners) {
                if (scanner == scanner2) {
                    continue;
                }

                max = Math.max(max, scanner.position.manhattanDistance(scanner2.position));
            }
        }

        return String.valueOf(max);
    }

    private ArrayList<Scanner> processScanners(String input) {
        var scannersInput = input.split("\n\n");
        var scanners = new ArrayList<Scanner>();

        for (var scannerInput : scannersInput) {
            var lines = Arrays.stream(scannerInput.split("\n")).collect(Collectors.toList());
            var scannerNumber = StringUtil.ints(lines.removeFirst()).getFirst();
            var points = new ArrayList<Point3D>();
            var beacons = new HashMap<Point3D, Beacon>();

            for (var line : lines) {
                var coordinates = StringUtil.sints(line);
                points.add(new Point3D(coordinates.get(0), coordinates.get(1), coordinates.get(2)));
            }

            for (var i = 0; i < points.size(); i++) {
                var beacon = new Beacon(points.get(i));

                for (var j = 0; j < points.size(); j++) {
                    if (i != j) {
                        beacon.distanceToOtherBeacons.add(beacon.position.manhattanDistance(points.get(j)));
                    }
                }

                beacons.put(beacon.position, beacon);
            }

            scanners.add(new Scanner(scannerNumber, beacons));
        }

        scanners.getFirst().position = new Point3D(0, 0, 0);

        while (scanners.stream().anyMatch(scanner -> scanner.position == null)) {
            for (var scanner1 : scanners) {
                if (scanner1.position == null) {
                    continue;
                }

                for (var scanner2 : scanners) {
                    if (scanner2.position != null) {
                        continue;
                    }

                    for (var scanner1beacon : scanner1.beacons.values()) {
                        for (var scanner2beacon : scanner2.beacons.values()) {
                            var commonDistancesCount = scanner1beacon.distanceToOtherBeacons
                                .stream()
                                .filter(distance -> scanner2beacon.distanceToOtherBeacons.contains(distance))
                                .count();

                            if (commonDistancesCount >= 11) {
                                scanner2beacon.matchingBeaconPosition = scanner1beacon.position;
                            }
                        }
                    }

                    var matchedBeacons = scanner2.beacons.values().stream().filter(b -> b.matchingBeaconPosition != null).toList();

                    if (matchedBeacons.size() < 12) {
                        for (var b : scanner2.beacons.values()) {
                            b.matchingBeaconPosition = null;
                        }

                        continue;
                    }

                    var beacon1 = matchedBeacons.get(0);
                    var beacon2 = matchedBeacons.get(1);
                    var diffX = beacon1.position.x - beacon2.position.x;
                    var diffY = beacon1.position.y - beacon2.position.y;
                    var diffZ = beacon1.position.z - beacon2.position.z;
                    var targetDiffX = beacon1.matchingBeaconPosition.x - beacon2.matchingBeaconPosition.x;
                    var targetDiffY = beacon1.matchingBeaconPosition.y - beacon2.matchingBeaconPosition.y;
                    var targetDiffZ = beacon1.matchingBeaconPosition.z - beacon2.matchingBeaconPosition.z;

                    var map = new HashMap<Integer, CoordinateConverter>() {{
                        put(diffX, (pos) -> pos.x);
                        put(-diffX, (pos) -> -pos.x);
                        put(diffY, (pos) -> pos.y);
                        put(-diffY, (pos) -> -pos.y);
                        put(diffZ, (pos) -> pos.z);
                        put(-diffZ, (pos) -> -pos.z);
                    }};

                    scanner2.xConverter = map.get(targetDiffX);
                    scanner2.yConverter = map.get(targetDiffY);
                    scanner2.zConverter = map.get(targetDiffZ);

                    var convertedBeacon1 = new Point3D(
                        scanner2.xConverter.convert(beacon1.position),
                        scanner2.yConverter.convert(beacon1.position),
                        scanner2.zConverter.convert(beacon1.position)
                    );

                    scanner2.position = new Point3D(
                        beacon1.matchingBeaconPosition.x - convertedBeacon1.x,
                        beacon1.matchingBeaconPosition.y - convertedBeacon1.y,
                        beacon1.matchingBeaconPosition.z - convertedBeacon1.z
                    );

                    for (var beacon : scanner2.beacons.values()) {
                        beacon.position = new Point3D(
                            scanner2.xConverter.convert(beacon.position) + scanner2.position.x,
                            scanner2.yConverter.convert(beacon.position) + scanner2.position.y,
                            scanner2.zConverter.convert(beacon.position) + scanner2.position.z
                        );
                    }
                }
            }
        }

        return scanners;
    }
}

class Scanner {
    public int number;
    public HashMap<Point3D, Beacon> beacons;
    public Point3D position = null;
    public CoordinateConverter xConverter;
    public CoordinateConverter yConverter;
    public CoordinateConverter zConverter;

    public Scanner(int number, HashMap<Point3D, Beacon> beacons) {
        this.number = number;
        this.beacons = beacons;
    }
}

class Beacon {
    public Point3D position;
    public ArrayList<Long> distanceToOtherBeacons = new ArrayList<>();
    public Point3D matchingBeaconPosition = null;

    public Beacon(Point3D position) {
        this.position = position;
    }
}

interface CoordinateConverter {
    int convert(Point3D position);
}