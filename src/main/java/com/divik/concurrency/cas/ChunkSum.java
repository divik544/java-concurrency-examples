package com.divik.concurrency.cas;

import lombok.Getter;

@Getter
public class ChunkSum {
    private long sum;

    public ChunkSum(long sum) {
        this.sum = sum;
    }

    // synchronized keyword implicitly uses object locks, which are blocking
    public synchronized void add(long value) {
        sum += value;
    }
}
