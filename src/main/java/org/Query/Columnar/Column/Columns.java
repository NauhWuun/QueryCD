package org.Query.Columnar.Column;

import org.Query.Columnar.Bytes;
import org.Query.Columnar.CompactHashSet;
import org.Query.Columnar.CompactSetTranslator;
import org.Query.Columnar.Util;

import java.io.IOException;
import java.io.Serializable;

public class Columns<T extends Serializable>
{
    private final Long index;
    private final String columnName;
    private final ColumnType type;
    private final Integer length;

    private static final  CompactSetTranslator<Object> TRANSLATOR = new CompactSetTranslator<Object>() {
        public byte[] serialize(Object s) {
            try {
                return Bytes.convertToByteArray(String.valueOf(s));
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        public boolean isInstance(Object obj) {
            return true;
        }

        public int getHash(Object s) {
            return Util.SDBMHash(String.valueOf(s));
        }

        public Object deserialize(byte[] packed) {
            try {
                return Bytes.ConvertBytesToString(packed);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    };

    private final CompactHashSet<Object> values = new CompactHashSet<>(TRANSLATOR);

    public Columns(Long index, String columnName, ColumnType type, T value, Integer length) {
        this.index = index;
        this.columnName = columnName;
        this.type = type;
        this.length = length;
        this.values.add(value);
    }

    public Long getIndex() {
        return index;
    }

    public String getName() {
        return columnName;
    }

    public ColumnType getType() {
        return type;
    }

    public Integer getLength() {
        return length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o || ! (o instanceof Columns)) {
            return true;
        }

        Columns<T> columns = (Columns<T>) o;
        if (! columnName.equals(columns.columnName)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = columnName.hashCode();
        result = 31 * result + 0x88d43287;
        result = 31 * result;
        return result;
    }

    @Override
    public String toString() {
        return "Column {"
                + "columnName= " + this.getName()     + " "
                + "columnType= " + this.getType()     + " "
                + "Value= "      + values.toString()  + " "
                + "Length= "     + this.getLength()   + " "
                + '}';
    }
}