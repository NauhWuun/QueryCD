package org.Query.Columnar.Column;

public class IndexPart
{
	private final String column;
	private final int prefixLen;

	public IndexPart(String column, int prefixLen) {
		this.column = column;
		this.prefixLen = prefixLen;
	}

	public String getColumn() {
		return column;
	}
	public int getPrefixLen() {
		return prefixLen;
	}
}