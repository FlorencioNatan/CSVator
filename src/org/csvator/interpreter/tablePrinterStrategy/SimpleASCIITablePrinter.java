package org.csvator.interpreter.tablePrinterStrategy;

public class SimpleASCIITablePrinter implements TablePrinterStrategy {

	@Override
	public void printTable(String[][] table, String[] header) {
		int[] tableSize = this.calculateColumnSize(table, header);
		table = this.formatColumns(table, tableSize);
		header = this.formatHeader(header, tableSize);
		StringBuilder content = new StringBuilder();
		String blankLine = "";

		for (int i = 0; i < tableSize.length; i++) {
			blankLine += "+" + "-".repeat(tableSize[i] + 2);
		}
		blankLine += "+\n";

		if (header != null) {
			content.append(blankLine);
			content.append("| " + String.join(" | ", header) + " |\n");
			content.append(blankLine);
		}

		for (int i = 0; i < table.length; i++) {
			content.append("| " + String.join(" | ", table[i]) + " |\n");
		}
		content.append(blankLine);

		System.out.println(content);
	}

	private int[] calculateColumnSize(String[][] table, String[] header) {
		int[] size = new int[table[0].length];

		for(int i = 0; i < table.length; i++) {
			for(int j = 0; j < size.length; j++) {
				if (table[i][j].length() > size[j]) {
					size[j] = table[i][j].length();
				}
			}
		}

		for(int i = 0; i < size.length; i++) {
			if (header[i].length() > size[i]) {
				size[i] = header[i].length();
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

	private String[] formatHeader(String[] header, int[] tableSize) {
		for(int i = 0; i < tableSize.length; i++) {
			header[i] = String.format("%-" + tableSize[i] + "s", header[i]);
		}

		return header;
	}

}
