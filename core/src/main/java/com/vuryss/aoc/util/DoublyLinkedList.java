package com.vuryss.aoc.util;

public class DoublyLinkedList<T> {
    public DoublyLinkedList<T> next;
    public DoublyLinkedList<T> previous;
    public T value;

    public DoublyLinkedList(T value) {
        this.value = value;
        this.next = this;
        this.previous = this;
    }

    public DoublyLinkedList(T value, DoublyLinkedList<T> next, DoublyLinkedList<T> previous) {
        this.value = value;
        this.next = next;
        this.previous = previous;
    }

    public DoublyLinkedList<T> insertNext(T value) {
        var newNode = new DoublyLinkedList<>(value, this.next, this);
        this.next = newNode;

        if (null != newNode.next) {
            newNode.next.previous = newNode;
        }

        return newNode;
    }

    public DoublyLinkedList<T> removePrevious() {
        var prev = this.previous;
        prev.previous.next = this;
        this.previous = prev.previous;

        return prev;
    }

    public DoublyLinkedList<T> forward(int steps) {
        var node = this;

        while (steps-- > 0) {
            node = node.next;
        }

        return node;
    }

    public DoublyLinkedList<T> backward(int steps) {
        var node = this;

        while (steps-- > 0) {
            node = node.previous;
        }

        return node;
    }
}
