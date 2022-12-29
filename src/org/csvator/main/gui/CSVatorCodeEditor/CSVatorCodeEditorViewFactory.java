package org.csvator.main.gui.CSVatorCodeEditor;

import javax.swing.text.Element;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;

public class CSVatorCodeEditorViewFactory implements ViewFactory {

	@Override
	public View create(Element elem) { 
		return new CSVatorCodeEditorView(elem);
	}

}
