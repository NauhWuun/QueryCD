package org.Query.Columnar;

import java.text.SimpleDateFormat;
import java.util.*;

public class SubColumn
{
    private final Map<Object, Object[]> childNodes;
    private final Map<Object, Integer> childNodeNumbers;

    private final String subTreeName;
    private final SubColumnNumberIndex nodeIndex;
    private final SubColumnTimes timeIndex;

    public SubColumn(final String subTreeName) {
        this.subTreeName = subTreeName;

        childNodes = new IdentityHashMap<>();
        childNodeNumbers = new WeakHashMap<>();
        nodeIndex = new SubColumnNumberIndex();
        timeIndex = new SubColumnTimes(TimeStamp.date2Stamp(
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss z").format(new Date())));
    }

    public <K, V extends Comparable<? super V>> Map<Object, Integer> downSort(Map<K, V> map) {
        List<Map.Entry<Object, Integer>> list = new LinkedList<>(childNodeNumbers.entrySet());
        list.sort((o1, o2) -> -(o1.getValue()).compareTo(o2.getValue()));

        Map<Object, Integer> result = new LinkedHashMap<Object, Integer>();
        for (Map.Entry<Object, Integer> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

    public <K, V extends Comparable<? super V>> Map<Object, Integer> upSort(Map<K, V> map) {
        List<Map.Entry<Object, Integer>> list = new LinkedList<>(childNodeNumbers.entrySet());
        list.sort(Map.Entry.comparingByValue());

        Map<Object, Integer> result = new LinkedHashMap<>();
        for (Map.Entry<Object, Integer> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

    public void addChildTreeData(final Object key, Object... value) {
        childNodes.put(key, value);
        childNodeNumbers.put(key, nodeIndex.moveOn());
        timeIndex.setUpdateTime(
                (int) TimeStamp.date2Stamp(
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss z").format(new Date())),
                key,
                value
        );
    }

    public void lazyChildTreeData(final Object key) {
        timeIndex.setUpdateTime(
                (int) TimeStamp.date2Stamp(
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss z").format(new Date())),
                key,
                timeIndex.getInnerValue(key)
        );
    }

    public void removeChildTreeData(final Object key) {
        childNodes.remove(key);
        childNodeNumbers.remove(key);

        nodeIndex.moveOff();
        timeIndex.removeInnerData((int) TimeStamp.date2Stamp(
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss z").format(new Date())));
    }

    public static SubColumn Builder(final String subTreeName) {
        return new SubColumn(subTreeName);
    }

    public boolean hasChildTreeKey(Object key) { return childNodes.containsKey(key); }

    public final String getCurrentSubTreeName() {
        return subTreeName;
    }

    public Object getValue(Object key) {
        return childNodes.get(key);
    }

    public long getCurrentTreeNUmber() {
        return nodeIndex.get();
    }

    public int getSubNodeTime() { return timeIndex.getCreateTime(); }

    public int getUpdateNodeTime() {
        return timeIndex.getUpdateTime();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (! (o instanceof Columnar)) return false;

        Columnar col = (Columnar) o;
        return this.hashCode() == col.hashCode();
    }

    @Override
    public int hashCode() {
        return RockRand.nextInt();
    }
}