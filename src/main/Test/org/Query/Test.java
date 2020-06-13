package org.Query;

import org.Query.Columnar.Column.ColumnType;
import org.Query.Columnar.Columnar;

public class Test
{
    public static void main(String... args) {
	    Columnar col = new Columnar("test");
	    col.addColumn("test-1", ColumnType.CHAR, "test1", 32);
	    col.addColumn("test-2", ColumnType.DOUBLE, 3.14,32);
	    col.addColumn("test-3", ColumnType.FLOAT, 3.0,8);
	    col.addColumn("test-4", ColumnType.LONGVARBINARY, 1,8);
	    col.addColumn("test-5", ColumnType.INTEGER, 123,4);
	    col.addColumn("test-6", ColumnType.BLOB, "*",4);

	    col.addIndex("test1-test2", "test-1", 0);

	    col.build().getColDefs().forEach(k -> System.out.println(k.toString()));
	}
}