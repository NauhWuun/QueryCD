package org.Query.Columnar.Column;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Columns<T extends Serializable>
{
    private final String columnName;
    
    private final int MAX_VALUE = Integer.MAX_VALUE / 8;
    private final double factor = 0.77;

    private final ArrayList<T> vals = new ArrayList<T>();

    public Columns(String columnName) {
        this.columnName = columnName;
    }

    public Columns(String columnName, T val) {
        this.columnName = columnName;
        this.add(val);
    }

    public void add(T val) {
        if (vals.size() * factor >= MAX_VALUE) {
            
        }

        vals.add(val);
    }

    public String getColumnName() {
        return columnName;
    }

    public List<T> getVals() {
        return vals;
    }

    public void allClear() {
        vals.clear();
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