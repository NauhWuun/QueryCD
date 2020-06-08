package org.Query.Columnar;

import org.Query.Columnar.Column.Columns;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Columnar
{
	private ArrayList<Columns> columns;
	public static BloomFilter familyFilter;

	private final Path db;
	private static final String DB_FILENAME = "./query.cd";

	public Columnar() {
		db = Paths.get(DB_FILENAME);

		// if (! Files.exists(db))
		// 	throw new RuntimeException("Where Is Local DB File???");

		familyFilter = new BloomFilter(Integer.MAX_VALUE / 8, Integer.MAX_VALUE / 2);
	}

	private ArrayList<Columns> columns() {
		if (this.columns == null) {
			this.columns = new ArrayList<>();
		}
		return this.columns;
	}

	public <T> void addColumn(String family, T value /* default compress */) {
		columns().add(new Columns<T>(family, value));
	}

	public static void main(String... args) {
		Columnar col = new Columnar();

		for (int i = 0; i < 100000000; i++) {
			col.addColumn("", i);
		}

		col.getColumns().forEach(k -> {
			System.out.println(k.toString());
		});
	}

	public void saveToFile(final String dataFile) throws IOException {
		
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