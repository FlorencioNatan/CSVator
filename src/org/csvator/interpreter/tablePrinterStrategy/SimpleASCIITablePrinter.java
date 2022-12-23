package org.csvator.interpreter.tablePrinterStrategy;

public class SimpleASCIITablePrinter implements TablePrinterStrategy {

	@Override
	public void printTable(String[][] table) {
		int[] tableSize = this.calculateColumnSize(table);
		table = this.formatColumns(table, tableSize);
		StringBuilder content = new StringBuilder();
		String blankLine = "";

		for (int i = 0; i < tableSize.length; i++) {
			blankLine += "+" + "-".repeat(tableSize[i] + 2);
		}
		blankLine += "+\n";

		//Header
		content.append(blankLine);
		content.append("| " + String.join(" | ", table[0]) + " |\n");
		content.append(blankLine);

		//Body
		for (int i = 1; i < table.length; i++) {
			content.append("| " + String.join(" | ", table[i]) + " |\n");
		}
		content.append(blankLine);

		System.out.println(content);
	}

	private int[] calculateColumnSize(String[][] table) {
		int[] size = new int[table[0].length];

		for(int i = 0; i < table.length; i++) {
			for(int j = 0; j < size.length; j++) {
				if (table[i][j].length() > size[j]) {
					size[j] = table[i][j].length();
				}
			}
		}

		return size;
	}

	private String[][] formatColumns(String[][] table, int[] tableSize) {
		for(int i = 0; i < table.length; i++) {
			for(int j = 0; j < tableSize.length; j++) {
				table[i][j] = String.format("%-" + tableSize[j] + "s", table[i][j]);
			}
		}

		return table;
	}

}
