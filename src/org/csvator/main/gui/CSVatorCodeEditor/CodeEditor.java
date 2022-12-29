package org.csvator.main.gui.CSVatorCodeEditor;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JTextPane;

public class CodeEditor extends JTextPane {

	private static final long serialVersionUID = -1905385228124505021L;

	public CodeEditor() {
		this.setFont(new Font("Fira Code Retina", Font.PLAIN, 14));
		this.setEditorKitForContentType("text/xml", new CSVatorCodeEditorKit());
		this.setContentType("text/xml");
		this.setBackground(new Color(40, 40, 52));
		this.setForeground(new Color(171, 178, 191));
		this.setCaretColor(new Color(97, 175, 239));
	}
}
