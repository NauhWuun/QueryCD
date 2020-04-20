package org.Query.Columnar;

import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Columnar
{
    private static final Map<String, SubColumn> columnMaps = new ConcurrentHashMap<>();
    private static final ColumnTreeIndex<String> columnIndex = new ColumnTreeIndex<>(getKeys());

    private final Globalndex<Object, Object> columnFilter;

    public Columnar() { columnFilter = new Globalndex<>(); }

    public void createColumnFamily(final String newColumnName) throws ParseException {
        if (columnMaps.containsKey(newColumnName))
            return;

        columnMaps.put(newColumnName, new SubColumn(newColumnName));
    }

    public void addtionColumnData(final String columnName, final Object key, final Object value) throws ParseException {
        if (! columnMaps.containsKey(columnName))
            return;

        columnMaps.get(columnName).addChildTreeData(key, value);

        if (! columnFilter.ContainsKey(key) && ! columnFilter.ContainsValue(value))
            columnFilter.pushKeyIndexData(key).pushValueIndexData(value);
    }

    @Deprecated
    public void Remove(final String columnName) {
//        if (! columnMaps.containsKey(columnName))
//            return;
//        columnMaps.remove(columnName);
    }

    public void lazyRemoveChildTreeData(final String columnName, final Object key) throws ParseException {
        if (! columnMaps.containsKey(columnName))
            return;

        columnMaps.get(columnName).lazyChildTreeData(key);
    }

    public void removeColumnData(final String columnName, final Object key) throws ParseException {
        if (! columnMaps.containsKey(columnName))
            return;

        columnMaps.get(columnName).removeChildTreeData(key);
    }

    public SubColumn getSubColumn(String columnName) {
        return columnMaps.get(columnName);
    }

    public List<SubColumn> getParentSubList(String columnName) {
        return columnMaps.values()
                .parallelStream()
                .filter(x -> columnName.equalsIgnoreCase(x.getCurrentSubTreeName()))
                .collect(Collectors.toList());
    }

    public Object getValueWithParentAndKey(String columnName, Object key) {
        final Object[] returnValue = { null };

        columnMaps.values()
                .parallelStream()
                .forEach(x -> {
                    if (columnName.equalsIgnoreCase(x.getCurrentSubTreeName()) && x.hasChildTreeKey(key))
                        returnValue[0] = x.getValue(key);
                });

        return returnValue[0];
    }

    public static List<Object> getValues() {
        return columnMaps.values().parallelStream().collect(Collectors.toList());
    }

    public static List<String> getKeys() {
        return columnMaps.keySet().parallelStream().collect(Collectors.toList());
    }

    public boolean contains(String columnName) {
        return columnMaps.containsKey(columnName);
    }

    public final int size() {
        return columnMaps.size();
    }

    public Object previous() {
        return columnIndex.previous();
    }

    public Object next() {
        return columnIndex.next();
    }

    public boolean hasNext() {
        return columnIndex.hasNext();
    }

    public boolean hasPrevious() {
        return columnIndex.hasPrevious();
    }

    public int nextIndex() {
        return columnIndex.nextIndex();
    }

    public int previousIndex() {
        return columnIndex.previousIndex();
    }

    public boolean hasKey(Object key) {
        return columnFilter.ContainsValue(key);
    }

    public boolean hasValue(Object value) {
        return columnFilter.ContainsValue(value);
    }
}