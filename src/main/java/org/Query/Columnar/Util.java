package org.Query.Columnar;

import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;

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
		throw new RuntimeException("Unknown sqlType " + type);
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
				return byte[].class;
			case Types.BIT:
				return Boolean.class;
			case Types.TINYINT:
			case Types.SMALLINT:
				return Short.class;
			case Types.INTEGER:
				return Integer.class;
			case Types.BIGINT:
				return Long.class;
			case Types.REAL:
				return Float.class;
			case Types.DOUBLE:
			case Types.FLOAT:
				return Double.class;
			case Types.DATE:
				return Date.class;
			case Types.TIME:
				return Time.class;
			case Types.TIMESTAMP:
				return Timestamp.class;
			default:
				throw new RuntimeException("We do not support tables with Type: " + getTypeName(type));
		}
	}

	public static int SDBMHash(byte[] str) {
		int hash = 0;
		for (byte b : str) {
			hash = b + (hash << 6) + (hash << 16) - hash;
		}
		return (hash & 0x7FFFFFFF);
	}
}