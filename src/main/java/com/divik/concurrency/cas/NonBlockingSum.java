package com.divik.concurrency.cas;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

public class NonBlockingSum {
    private static final int TOTAL_SIZE = 20000000;

    private static final int CHUNK_SIZE = 2000;

    public static void main(String[] args) throws InterruptedException {

        final int NUMBER_OF_CHUNKS = TOTAL_SIZE / CHUNK_SIZE;
        int[] arr = IntStream.generate(() -> ThreadLocalRandom.current().nextInt(0, 50)).limit(TOTAL_SIZE).toArray();
        System.out.println("expected: " + IntStream.of(arr).sum());
        AtomicLong chunkSum = new AtomicLong(0);
        ExecutorService executorService = Executors.newFixedThreadPool(30);
        long currentTime = System.currentTimeMillis();
        for (int i = 0; i < NUMBER_OF_CHUNKS; i++) {
            int start = i * CHUNK_SIZE;
            int end = Math.min((i + 1) * CHUNK_SIZE, TOTAL_SIZE);
            executorService.submit(() -> {
                long temp = Arrays.stream(arr, start, end).sum();
                long old, newSum;
                do {
                    old = chunkSum.get();
                    newSum = old + temp;
                } while (!chunkSum.compareAndSet(old, newSum));
            });
        }
        executorService.shutdown();
        System.out.println(executorService.awaitTermination(5, java.util.concurrent.TimeUnit.SECONDS));
        System.out.println("Sum: " + chunkSum.get());
        System.out.println("Time taken: " + (System.currentTimeMillis() - currentTime) + "ms");
    }
}
