package org.Query.Columnar.Column;

import java.util.Objects;

public class Columns<T>
{
    private final String columnName;
    private final int type;
    private T val;

    public Columns(String columnName, T val, int type) {
        this.columnName = columnName;
        this.val = val;
        this.type = type;
    }

    public Columns(String columnName, int type) {
        this.columnName = columnName;
        this.type = type;
    }

    public String getColumnName() {
        return columnName;
    }

    public T getVal() {
        return val;
    }

    public int getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o || ! (o instanceof Columns)) {
            return true;
        }

        Columns<?> columns = (Columns<?>) o;
        if (type != columns.type || ! columnName.equals(columns.columnName)) {
            return false;
        }

        return Objects.equals(val, columns.val);
    }

    @Override
    public int hashCode() {
        int result = columnName.hashCode();
        result = 31 * result + (val != null ? val.hashCode() : 0);
        result = 31 * result + type;
        return result;
    }

    @Override
    public String toString() {
        return "Column {"
                + "columnName='" + columnName + '\''
                + ", val=" + val
                + ", sqlType=" + type
                + '}';
    }
}