package com.vuryss.aoc.solutions.event2022.day22;

public class CubeSide {
    public CubeSector[][] sectors;
    public boolean isMapped = false;
    public int rotationsBeforeMapping = 0;
    public int rotationsDuringWalking = 0;
    public boolean recordWalkingRotations = false;

    public CubeSide(byte size) {
        sectors = new CubeSector[size][size];
        for (var i = 0; i < size; i++) for (var j = 0; j < size; j++) sectors[i][j] = new CubeSector();
    }
}