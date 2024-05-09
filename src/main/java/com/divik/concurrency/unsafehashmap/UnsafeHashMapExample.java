package com.divik.concurrency.unsafehashmap;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * HashMap is thread unsafe.
 * This example demonstrates that by showing that during resizing of hashmap, the existing elements are not found.
 * In a single thread this operation is blocking, hence goes unnoticed.
 * Code Ref: https://stackoverflow.com/questions/18542037/how-to-prove-that-hashmap-in-java-is-not-thread-safe
 */
public class UnsafeHashMapExample {

    public static void main(String[] args) {
        final Map<Integer, String> map = new HashMap<>();

        final Integer targetKey = 0b1111_1111_1111_1111; // 65 535
        final String targetValue = "v";
        map.put(targetKey, targetValue);

        new Thread(() -> {
            IntStream.range(0, targetKey).forEach(key -> map.put(key, "someValue"));
        }).start();


        while (true) {
            if (!targetValue.equals(map.get(targetKey))) {
                throw new RuntimeException("HashMap is not thread safe.");
            }
        }
    }
}
