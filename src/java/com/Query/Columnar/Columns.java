package com.Query.Columnar;

import com.Query.Columnar.Index.ColumnTreeIndex;
import com.Query.Columnar.Index.Globalndex;

import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Columns
{
    private static final Columns colTree = new Columns();
    private static final Map<String, SubColumn> columnMaps = new ConcurrentHashMap<>();
    private static final ColumnTreeIndex<String> columnIndex = new ColumnTreeIndex<String>(colTree.getKeys());

    private Globalndex<Object, Object> columnFilter;

    private Columns() {
        columnFilter = new Globalndex<>();
    }

    public void createColumnFamily(final String newColumnName, final Object key, final Object value) throws ParseException {
        columnMaps.put(newColumnName, new SubColumn(newColumnName).addChildTreeData(key, value));
        columnFilter.pushKeyIndexData(key).pushValueIndexData(value);
    }

    public void addtionColumnDatas(final String columnName, final Object key, final Object value) throws ParseException {
        columnMaps.get(columnName).addChildTreeData(key, value);

        if (! columnFilter.ContainsKey(key) && ! columnFilter.ContainsValue(value))
            columnFilter.pushKeyIndexData(key).pushValueIndexData(value);
    }

    public void delete(String columnName) {
        columnMaps.remove(columnName);
    }

    public void lazyRemoveChildTreeData(final String columnName, final Object key) throws ParseException {
        columnMaps.get(columnName).lazyChildTreeData(key);
    }

    public void removeColumnDatas(final String columnName, final Object key) throws ParseException {
        columnMaps.get(columnName).removeChildTreeData(key);
    }

    public SubColumn getOnlySubTree(String columnName) {
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

    public List<Object> getValues() {
        return columnMaps.values().parallelStream()
                .collect(Collectors.toList());
    }

    public List<String> getKeys() {
        return columnMaps.keySet().parallelStream()
                .collect(Collectors.toList());
    }

    public boolean contains(String columnName) {
        return columnMaps.containsKey(columnName);
    }

    public final int size() {
        return columnMaps.size();
    }

    public static Columns Builder() {
        return colTree;
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

    /**
     *
     * @param key : Global SubTreeNode K -> Key
     * @return
     *      true: hasKey
     *      false: non-key
     */
    public boolean hasKey(Object key) {
        return columnFilter.ContainsValue(key);
    }

    /**
     *
     * @param value : Global SubTreeNode V -> value
     * @return
     *      true: hasValue
     *      false: non-value
     */
    public boolean hasValue(Object value) {
        return columnFilter.ContainsValue(value);
    }

    /**
     *
     * @return
     *      return meta index object to ...
     */
    public Globalndex<Object, Object> getMetaIndex() {
        return columnFilter;
    }
}