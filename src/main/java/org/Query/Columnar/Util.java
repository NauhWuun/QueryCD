package org.Query.Columnar;

import java.lang.reflect.Field;
import java.sql.Types;
import java.util.List;

public class Util
{
	private static String getTypeName(int type) {
		try {
			Integer val = type;
			for (Field field : Types.class.getFields()) {
				if (val.equals(field.get(null))) {
					return field.getName();
				}
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Could not get TypeName ", e);
		}
		throw new RuntimeException("Unknown Type " + type);
	}

	public static Class getType(int type) {
		switch (type) {
			case Types.CHAR:
			case Types.VARCHAR:
			case Types.LONGVARCHAR:
				return String.class;
			case Types.BINARY:
			case Types.VARBINARY:
			case Types.LONGVARBINARY:
			case Types.BLOB:
				return byte[].class;
			case Types.BIT:
			case Types.BOOLEAN:
				return Boolean.class;
			case Types.INTEGER:
			case Types.SMALLINT:
			case Types.DECIMAL:
			case Types.NUMERIC:
				return Integer.class;
			case Types.REAL:
				return Float.class;
			case Types.DOUBLE:
			case Types.FLOAT:
				return Double.class;
			default:
				throw new RuntimeException("We do not support tables with Type: " + getTypeName(type));
		}
	}

	public static int SDBMHash(String str) {
		int hash = 0;

		for (int i = 0; i < str.length(); i++) {
			hash = str.charAt(i) + (hash << 6) + (hash << 16) - hash;
		}

		return (hash & 0x7FFFFFFF);
	}
}