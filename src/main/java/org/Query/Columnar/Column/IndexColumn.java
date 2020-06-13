package org.Query.Columnar.Column;

import java.util.List;
import java.util.Map;

public class IndexColumn
{
	private final String name;
	private final List<Columns> columns;
	private final Map<String, Integer> prefixLenOverrides;

	public IndexColumn(String name, List<Columns> columns, Map<String, Integer> prefixLenOverrides) {
		this.name = name;
		this.columns = columns;
		this.prefixLenOverrides = prefixLenOverrides;
	}

	public String getName() {
		return name;
	}

	public List<Columns> getColumns() {
		return columns;
	}

	public Map<String, Integer> getPrefixLenOverrides() {
		return prefixLenOverrides;
	}
}