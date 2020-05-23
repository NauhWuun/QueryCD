package org.Query.Columnar;

import org.Query.Columnar.Column.Columns;

import java.util.ArrayList;
import java.util.List;

public class Columnar
{
	private ArrayList<Columns> columns;
	private int rowID = 1;

	private ArrayList<Columns> columns() {
		if (this.columns == null) {
			this.columns = new ArrayList<>();
		}
		return this.columns;
	}

	public <T> Columnar addColumn(String family, T value) {
		columns().add(new Columns<T>(family, value, Integer.parseInt(value.getClass().toString())));
		++rowID;
		return this;
	}

	public boolean hasColumns() {
		return this.columns != null;
	}

	public List<Columns> getColumns() {
		return this.columns;
	}

	public int getRowID() {
		return rowID;
	}
}