package com.Query.Columnar.Index;

import java.util.concurrent.atomic.AtomicInteger;

public final class SubColumnNumberIndex
{
    private AtomicInteger currentIndex;

    public SubColumnNumberIndex() {
        currentIndex = new AtomicInteger();
    }

    public int moveOn() {
        return currentIndex.getAndIncrement();
    }

    public int moveOff() {
        return currentIndex.decrementAndGet();
    }

    public final long get() {
        return currentIndex.get();
    }
}