package org.csvator.interpreter.tablePrinterStrategy;

public class SimpleCSVTablePrinter implements TablePrinterStrategy {

	@Override
	public void printTable(String[][] table) {
		StringBuilder content = new StringBuilder();

		for (String[] strings : table) {
			content.append(String.join(",", strings));
			content.append("\n");
		}

		System.out.println(content);
	}

}
