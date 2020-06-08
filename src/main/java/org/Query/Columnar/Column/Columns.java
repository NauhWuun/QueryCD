package org.Query.Columnar.Column;

import java.util.ArrayList;
import java.util.List;

import org.roaringbitmap.RoaringBitmap;

public class Columns<T>
{
    private final String columnName;
    
    private static final RoaringBitmap rb = new RoaringBitmap();
    private static final int MAX_VALUE = Integer.MAX_VALUE / 8;
    private static final double factor = 0.77;

    private static final ArrayList<Object> vals = new ArrayList<Object>(MAX_VALUE);

    public Columns(String columnName, T val) {
        if (vals.size() * factor == MAX_VALUE)
            throw new IllegalArgumentException("the size of the array is larger");

        this.columnName = columnName;
        this.add(val);
    }

    public void add(T val) {
        if (val != null)
            vals.add(val);
    }

    public String getColumnName() {
        return columnName;
    }

    public static List<Object> getVals() {
        return vals;
    }

    public boolean getVal(T val) {
        return vals.contains(val);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o || ! (o instanceof Columns)) {
            return true;
        }

        Columns<?> columns = (Columns<?>) o;
        if (! columnName.equals(columns.columnName)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = columnName.hashCode();
        result = 31 * result + (vals != null ? vals.hashCode() : 0x81010101);
        result = 31 * result;
        return result;
    }

    @Override
    public String toString() {
        return "Column {"
                + "columnName='" + columnName + '\''
                + ", vals=" + vals
                + '}';
    }
}