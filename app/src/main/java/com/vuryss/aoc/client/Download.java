package com.vuryss.aoc.client;

public sealed interface Download<T> {
    record Started<T>() implements Download<T> {}
    record Done<T>(T value) implements Download<T> {}
}
