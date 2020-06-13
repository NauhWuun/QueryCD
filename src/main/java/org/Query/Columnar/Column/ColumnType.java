package org.Query.Columnar.Column;

public enum ColumnType
{
	STRING(0x0),
	BLOB(0x1),
	INTEGER(0x2),
	FLOAT(0x3),
	DOUBLE(0x4),
	CHAR(0x5),
	VARCHAR(0x6),
	LONGVARCHAR(0x7),
	BINARY(0x8),
	VARBINARY(0x9),
	LONGVARBINARY(0x10),
	REAL(0x11);

	private final int code;

	ColumnType(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public static ColumnType fromCode(int code) {
		return ColumnType.values()[code];
	}

	public boolean isStringType() { return this.code == 0x0; }

	public boolean isBlobType() { return this.code == 0x1; }

	public boolean isIntegerType() { return this.code == 0x2; }

	public boolean isFloatType() {
		return this.code == 0x3;
	}

	public boolean isDoubleType() {
		return this.code == 0x4;
	}
}