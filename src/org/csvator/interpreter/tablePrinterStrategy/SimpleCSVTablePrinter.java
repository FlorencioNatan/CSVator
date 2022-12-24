package org.csvator.interpreter.tablePrinterStrategy;

public class SimpleCSVTablePrinter implements TablePrinterStrategy {

	@Override
	public void printTable(String[][] table, String[] header) {
		StringBuilder content = new StringBuilder();

		if (header != null) {
			content.append(String.join(",", header));
		}

		for (String[] strings : table) {
			content.append(String.join(",", strings));
			content.append("\n");
		}

		System.out.println(content);
	}

}
