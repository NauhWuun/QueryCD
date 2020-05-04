package org.Query.Columnar;

import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Columnar
{
    private static final Map<String, SubColumn> columnMaps = new ConcurrentHashMap<>();
    private final Globalndex<Object, Object> columnFilter;

    public Columnar() { columnFilter = Globalndex.Builder(); }

    public void createColumnFamily(final String newColumnName) throws ParseException {
        if (columnMaps.containsKey(newColumnName))
            return;

        columnMaps.put(newColumnName, SubColumn.Builder(newColumnName));
    }

    public void addtionColumnData(final String columnName, Object key, Object... value) throws ParseException {
        if (! columnMaps.containsKey(columnName)) {
            columnFilter.pushKeyIndexData(key).pushValueIndexData(value);
            columnMaps.get(columnName).addChildTreeData(key, value);
        }
    }

    public void lazyRemoveChildTreeData(final String columnName, Object key) throws ParseException {
        columnMaps.get(columnName).lazyChildTreeData(key);
    }

    public void removeColumnData(final String columnName, Object key) throws ParseException {
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
        return getKeys().listIterator().previous();
    }

    public Object next() {
        return getKeys().listIterator().next();
    }

    public boolean hasNext() {
        return getKeys().listIterator().hasNext();
    }

    public boolean hasPrevious() {
        return getKeys().listIterator().hasPrevious();
    }

    public int nextIndex() {
        return getKeys().listIterator().nextIndex();
    }

    public int previousIndex() {
        return getKeys().listIterator().previousIndex();
    }

    public boolean hasKey(Object key) {
        return columnFilter.ContainsValue(key);
    }

    public boolean hasValue(Object value) {
        return columnFilter.ContainsValue(value);
    }
}