package org.Query.Columnar;

import java.util.BitSet;
import java.util.Iterator;
import java.util.Random;

public class BloomFilter implements Cloneable
{
    private BitSet hashes;
    private RandomInRange prng;

    private int k;                                          // Number of hash functions
    private static final double LN2 = 0.6931471805599453;   // ln(2)

    public BloomFilter(int n, int m) {
        k = (int) Math.round(LN2 * m / n);

        if (k <= 0)
            k = 1;

        this.hashes = new BitSet(m);
        this.prng   = new RandomInRange(m, k);
    }

    public BloomFilter(int n) {
        this(n, 1024 * 1024 * 500 * 1);
    }

    public void add(Object o) {
        prng.init(o);

        for (RandomInRange r : prng)
            hashes.set(r.value);
    }

    public boolean contains(Object o) {
        prng.init(o);

        for (RandomInRange r : prng)
            if (! hashes.get(r.value))
                return false;

        return true;
    }

    public byte[] serialize() {
        return hashes.toByteArray();
    }

    public void deserialize(byte[] bytes) {
        hashes = BitSet.valueOf(bytes);
    }

    public void clear() {
        hashes.clear();
    }

    public BloomFilter clone() throws CloneNotSupportedException {
        return (BloomFilter) super.clone();
    }

    public int hashCode() {
        return hashes.hashCode() ^ k;
    }

    public boolean equals(BloomFilter other) {
        return this.hashes.equals(other.hashes) && this.k == other.k;
    }

    public void merge(BloomFilter other) {
        if (other.k != this.k || other.hashes.size() != this.hashes.size()) {
            throw new IllegalArgumentException("Incompatible bloom filters");
        }

        this.hashes.or(other.hashes);
    }

    private class RandomInRange implements Iterable<RandomInRange>, Iterator<RandomInRange>
    {
        private Random prng;

        private int max;    // Maximum value returned + 1
        private int count;  // Number of random elements to generate
        private int i = 0;  // Number of elements generated

        public int value;   // The current value

        RandomInRange(int maximum, int k) {
            max   = maximum;
            count = k;
            prng  = new Random();
        }

        public void init(Object o) {
            prng.setSeed(o.hashCode());
        }

        public Iterator<RandomInRange> iterator() {
            i = 0;
            return this;
        }

        public RandomInRange next() {
            i++;
            value = prng.nextInt() % max;

            if (value < 0)
                value = -value;

            return this;
        }

        public boolean hasNext() {
            return i < count;
        }
    }
}