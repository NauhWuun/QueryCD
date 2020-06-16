package org.Query.Columnar;

import org.Query.Columnar.Column.*;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class Columnar
{
	public static BloomFilter valueFilter;
	private static final String DB_FILENAME = "./query.cd";

	private final String name;
	
	private final Map<String, Columns> columns;
	private final Map<String, List<IndexPart>> indexes;
	private final AtomicLong index = new AtomicLong();
	private final ArrayList<TableDefine> tables;

	private Path db;

	public Columnar(String name) {
		this.name = name;
		this.columns = new LinkedHashMap<>();
		this.indexes = new LinkedHashMap<>();

		valueFilter = new BloomFilter(Integer.MAX_VALUE / 8, Integer.MAX_VALUE / 4);
	}
	
	private ArrayList<tables> columns() {
        	if (this.tables == null) {
            		this.tables = new ArrayList<tables>();
        	}
        	return this.tables;
    	}

	private void createDatabase() {
		db = Paths.get(DB_FILENAME);

		if (! Files.exists(db)) {
			try {
				Files.createFile(Paths.get(DB_FILENAME));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void dropDatabase() {
		if (! Files.exists(db))
			return;

		try {
			Files.delete(db);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static String getPrimaryIndex(Map<String, List<IndexPart>> defs) {
		for (Map.Entry<String, List<IndexPart>> def : defs.entrySet()) {
			return def.getKey();
		}

		return null;
	}

	public <T extends Serializable> void addColumn(String name, ColumnType type, T value, int length) {
		if (length <= 0)
			throw new IllegalArgumentException("failed length: " + length);

		Util.getType(type.getCode());

		Columns<T> def = new Columns<T>(index.getAndIncrement(), name, type, value, length);
		this.columns.put(def.getName(), def);

		valueFilter.add(type);
	}

	public void addIndex(String indexName, String column, int prefixLen) {
		if (! this.indexes.containsKey(indexName)) {
			this.indexes.put(indexName, new ArrayList<>());
		}

		this.indexes.get(indexName).add(new IndexPart(column, prefixLen));
	}

	public TableDefine build() {
		String primary = getPrimaryIndex(this.indexes);
		Map<String, IndexColumn> defs = createIndexDefMap(primary);

		TableDefine td = new TableDefine(name, columns, defs, defs.get(primary));
		columns().add(td);
		return td;
	}
	
	public List<TableDefine> getColumns() {
	    return tables;
	}

	private Map<String, IndexColumn> createIndexDefMap(String primary) {
		Map<String, IndexColumn> defs = new LinkedHashMap<>();

		for (Map.Entry<String, List<IndexPart>> entry : indexes.entrySet()) {
			Map<String, Columns> indexColumns = new LinkedHashMap<>();
			Map<String, Integer> prefixLenOverrides = new LinkedHashMap<>();

			for (IndexPart part : entry.getValue()) {
				if (part.getPrefixLen() != 0) {
					prefixLenOverrides.put(part.getColumn(), part.getPrefixLen());
				}

				indexColumns.put(part.getColumn(), this.columns.get(part.getColumn()));
			}

			for (IndexPart primaryPart : this.indexes.get(primary)) {
				String primaryColumn = primaryPart.getColumn();

				if (! indexColumns.containsKey(primaryColumn)) {
					indexColumns.put(primaryColumn, this.columns.get(primaryColumn));
				}
			}

			List<Columns> indexColumnList = new ArrayList<>(indexColumns.values());
			IndexColumn idx = new IndexColumn(entry.getKey(),
					Collections.unmodifiableList(indexColumnList),
					Collections.unmodifiableMap(prefixLenOverrides));

			defs.put(entry.getKey(), idx);
		}

		return defs;
	}

	public boolean contains(Object val) {
		return valueFilter.contains(val);
	}
}
