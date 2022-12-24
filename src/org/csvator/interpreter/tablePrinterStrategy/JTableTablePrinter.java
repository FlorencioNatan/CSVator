package org.csvator.interpreter.tablePrinterStrategy;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class JTableTablePrinter implements TablePrinterStrategy {

	private JTable jTable;

	public JTableTablePrinter(JTable table) {
		jTable = table;
	}

	@Override
	public void printTable(String[][] table, String[] header) {
		DefaultTableModel tableModel = new DefaultTableModel(table, header);
		jTable.setModel(tableModel);
	}

}
