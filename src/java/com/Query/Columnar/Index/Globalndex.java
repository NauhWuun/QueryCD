package com.Query.Columnar.Index;

import com.Query.Columnar.BloomFilter;

public final class Globalndex<K, V>
{
    private BloomFilter keyIndex;
    private BloomFilter valueIndex;

    public Globalndex() {
        keyIndex = new BloomFilter();
        valueIndex = new BloomFilter();
    }

    public Globalndex(int min, int max) {
        keyIndex = new BloomFilter(min, max);
        valueIndex = new BloomFilter(min, max);
    }

    public Globalndex<K, V> pushKeyIndexData(final K key) {
        keyIndex.Add(key);
        return this;
    }

    public Globalndex<K, V> pushValueIndexData(final V value) {
        valueIndex.Add(value);
        return this;
    }

    public boolean ContainsKey(Object o) {
        return keyIndex.Contains(o);
    }

    public boolean ContainsValue(Object o) {
        return valueIndex.Contains(o);
    }

    public void Clear() {
        keyIndex.clear();
        valueIndex.clear();
    }
}
