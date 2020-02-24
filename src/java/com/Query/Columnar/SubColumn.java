package com.Query.Columnar;

import com.Query.Columnar.Index.SubColumnNumberIndex;
import com.Query.Columnar.Index.SubColumnTimes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class SubColumn
{
    private final Map<Object, Object> childNodes;
    private Map<Object, Integer> childNodeNumbers;

    private final String subTreeName;
    private SubColumnNumberIndex nodeIndex;
    private SubColumnTimes timeIndex;

    private final int MAX_COLUMN_SIZE = 0x124f8; /* Max One Column Size is 150000 */

    public SubColumn(final String subTreeName) throws ParseException {
        this.subTreeName = subTreeName;

        childNodes = new IdentityHashMap<>(MAX_COLUMN_SIZE);
        childNodeNumbers = new TreeMap<>();

        nodeIndex = new SubColumnNumberIndex();
        timeIndex = new SubColumnTimes(TimeStamp.date2Stamp(
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss z").format(new Date())));
    }

    public <K, V extends Comparable<? super V>> Map<Object, Integer> downSort(Map<K, V> map) {
        List<Map.Entry<Object, Integer>> list = new LinkedList<>(childNodeNumbers.entrySet());
        Collections.sort(list, (o1, o2) -> -(o1.getValue()).compareTo(o2.getValue()));

        Map<Object, Integer> result = new LinkedHashMap<Object, Integer>();
        for (Map.Entry<Object, Integer> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

    public <K, V extends Comparable<? super V>> Map<Object, Integer> upSort(Map<K, V> map) {
        List<Map.Entry<Object, Integer>> list = new LinkedList<>(childNodeNumbers.entrySet());
        Collections.sort(list, Comparator.comparing(Map.Entry::getValue));

        Map<Object, Integer> result = new LinkedHashMap<>();
        for (Map.Entry<Object, Integer> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

    public SubColumn addChildTreeData(final Object key, Object value) throws ParseException {
        if (nodeIndex.moveOn() >= MAX_COLUMN_SIZE)
            throw new IllegalArgumentException("out of max-one-cloumn size...");

        childNodes.put(key, (value == null) ? (byte) 0 : value);
        childNodeNumbers.put(key, nodeIndex.moveOn());
        timeIndex.setUpdateTime(
                (int) TimeStamp.date2Stamp(
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss z").format(new Date())),
                key,
                value
        );

        return this;
    }

    public void lazyChildTreeData(final Object key) throws ParseException {
        timeIndex.setUpdateTime(
                (int) TimeStamp.date2Stamp(
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss z").format(new Date())),
                key,
                timeIndex.getInnerValue(key)
        );
    }

    public void removeChildTreeData(final Object key) throws ParseException {
        childNodes.remove(key);
        childNodeNumbers.remove(key);

        nodeIndex.moveOff();

        /**
         * We don't think true deleting data, the old data has snap-shot effect with current data
         */
        timeIndex.removeInnerData((int) TimeStamp.date2Stamp(
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss z").format(new Date())));
    }

    public boolean hasChildTreeKey(final Object key) {
        return childNodes.containsKey(key);
    }

    public boolean hasChildTreeValue(final Object value) { return childNodes.containsValue(value); }

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
}