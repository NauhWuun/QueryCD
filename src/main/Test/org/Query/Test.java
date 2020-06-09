package org.Query;

@Test
public class Test
{
    public static void main(String... args) {
		Columnar col = new Columnar();

		for (int i = 0; i < 100; i++) {
			col.addColumn("0", i);
			col.addColumn("0", i - 1);
			col.addColumn("0", i - 2);
		}

		col.getColumns().forEach(k -> {
			System.out.println(k.toString());
		});
	}
}