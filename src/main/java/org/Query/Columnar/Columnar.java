package org.Query.Columnar;

import org.Query.Columnar.Column.Columns;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Columnar
{
	private ArrayList<Columns> columns;
	public static BloomFilter valueFilter;

	private final Path db;
	private static final String DB_FILENAME = "./query.cd";

	public Columnar() {
		db = Paths.get(DB_FILENAME);

		if (! Files.exists(db))
			try {
				Files.createFile(Paths.get(DB_FILENAME));
			} catch (IOException e) {
				e.printStackTrace();
			}

		valueFilter = new BloomFilter(Integer.MAX_VALUE / 8, Integer.MAX_VALUE / 2);
	}

	private ArrayList<Columns> columns() {
		if (this.columns == null) {
			this.columns = new ArrayList<>();
		}
		return this.columns;
	}

	public <T extends Serializable> void addColumn(String family, T value /* default compress */) {
		columns().add(new Columns<T>(family, Optional.ofNullable(value).get()));
		valueFilter.add(value);
	}

	public <T> boolean contains(T val) {
		return valueFilter.contains(val);
	}

	public void saveToFile() throws IOException {
		
	}

	public void loadInFile() {

	}

	public boolean hasColumns() {
		return this.columns != null;
	}

	public List<Columns> getColumns() {
		return this.columns;
	}
}