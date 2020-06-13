package org.Query.Columnar.Column;

import java.util.*;

public class TableDefine
{
	private final String name;
	private final Map<String, Columns> columnDefs;
	private final Map<String, IndexColumn> indexDefs;
	private final List<Columns> colDefs;
	private final IndexColumn primaryIndex;

	public TableDefine(String name,
	                   Map<String, Columns> columnDefs,
	                   Map<String, IndexColumn> indexDefs,
	                   IndexColumn primaryIndex) {
		this.name = name;

		Map<String, Columns> newCols = new LinkedHashMap<>(columnDefs);
		this.columnDefs = Collections.unmodifiableMap(newCols);

		List<Columns> newColDefs = new ArrayList<>(newCols.values());
		this.colDefs = Collections.unmodifiableList(newColDefs);

		Map<String, IndexColumn> newIdxs = new LinkedHashMap<>(indexDefs);
		this.indexDefs = Collections.unmodifiableMap(newIdxs);

		this.primaryIndex = primaryIndex;
	}

	public String getName() {
		return name;
	}

	public Map<String, Columns> getColumnDefs() {
		return columnDefs;
	}

	public List<Columns> getColDefs() { return colDefs; }

	public Map<String, IndexColumn> getIndexDefs() {
		return indexDefs;
	}

	public IndexColumn getPrimaryIndex() {
		return primaryIndex;
	}

	public IndexColumn getIndexDef(String index) {
		return indexDefs.get(index);
	}
}
