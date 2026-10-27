package com.vuryss.aoc.solutions.event2017;

import com.vuryss.aoc.util.StringUtil;

public class KnotHash {
    private final int[] hash = new int[16];
    private static final char[] HEX_DIGITS = "0123456789abcdef".toCharArray();

    public KnotHash(String input) {
        // Initialize lengths
        String trimmedInput = input.trim();
        int lengthCount = trimmedInput.length();
        int[] lengths = new int[lengthCount + 5];

        for (int i = 0; i < lengthCount; i++) {
            lengths[i] = trimmedInput.charAt(i);
        }
        lengths[lengthCount] = 17;
        lengths[lengthCount + 1] = 31;
        lengths[lengthCount + 2] = 73;
        lengths[lengthCount + 3] = 47;
        lengths[lengthCount + 4] = 23;

        // Initialize list
        int[] list = new int[256];

        for (int i = 0; i < 256; i++) {
            list[i] = i;
        }

        // Sparse hash
        int position = 0, skipSize = 0, idx1, idx2, temp;

        for (int round = 0; round < 64; round++) {
            for (int length : lengths) {
                // Reverse sublist
                // We need to swap elements from `position` to `position + length - 1` handling circular wrapping
                for (int i = 0; i < length / 2; i++) {
                    idx1 = (position + i) & 255;
                    idx2 = (position + length - 1 - i) & 255;

                    temp = list[idx1];
                    list[idx1] = list[idx2];
                    list[idx2] = temp;
                }

                position = (position + length + skipSize) & 255;
                skipSize++;
            }
        }

        // Dense hash
        int xor;

        for (int i = 0; i < 16; i++) {
            xor = 0;

            for (int j = 0; j < 16; j++) {
                xor ^= list[i * 16 + j];
            }

            hash[i] = xor;
        }
    }

    public String hexForm() {
        StringBuilder hex = new StringBuilder(32);

        for (int b : hash) {
            hex.append(HEX_DIGITS[(b >> 4) & 0xF]);
            hex.append(HEX_DIGITS[b & 0xF]);
        }

        return hex.toString();
    }

    public String binaryForm() {
        return StringUtil.hex2bin(hexForm());
    }
}
