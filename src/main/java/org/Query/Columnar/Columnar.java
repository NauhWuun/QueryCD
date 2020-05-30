package org.Query.Columnar;

import org.Query.Columnar.Column.Columns;
import org.roaringbitmap.RoaringBitmap;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Columnar
{
	private ArrayList<Columns> columns;

	public static RoaringBitmap rb;
	public static BloomFilter bf;

	private final Path db;
	private final Path index;
	private static final String DB_FILENAME = "./query.cd";
	private static final String INDEX_FILENAME = "./query.index";

	public Columnar() {
		db = Paths.get(DB_FILENAME);
		index = Paths.get(INDEX_FILENAME);

		if (! Files.exists(db) && ! Files.exists(index))
			throw new RuntimeException("Where Is Local DB File???");

		rb = new RoaringBitmap();
		bf = new BloomFilter(Integer.MIN_VALUE, Integer.MAX_VALUE - 1);
	}

	private ArrayList<Columns> columns() {
		if (this.columns == null) {
			this.columns = new ArrayList<>();
		}
		return this.columns;
	}

	public <T> void addColumn(String family, T value, boolean isCrypt, boolean isCompress) {
		columns().add(new Columns<T>(family, value));
		bf.add(value);
	}

	public static void main(String... args) {
		Columnar col = new Columnar();

		for (int i = 0; i < 1000000000; i++) {
			col.addColumn(i + "", i, false, true);
		}

		col.getColumns().forEach(k -> {
			System.out.println(k.toString());
		});
	}

	public <T> boolean Contain(T val) { return bf.contains(val); }

	public void saveToFile(final String dataFile) throws IOException {
		bf.clear();
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