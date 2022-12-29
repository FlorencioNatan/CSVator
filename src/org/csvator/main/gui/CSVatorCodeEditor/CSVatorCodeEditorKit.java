package org.csvator.main.gui.CSVatorCodeEditor;

import javax.swing.text.StyledEditorKit;
import javax.swing.text.ViewFactory;

public class CSVatorCodeEditorKit extends StyledEditorKit{

	private static final long serialVersionUID = -8278135773450863002L;
	private CSVatorCodeEditorViewFactory codeEditorViewFactory;

	public CSVatorCodeEditorKit() {
		codeEditorViewFactory = new CSVatorCodeEditorViewFactory();
	}

	@Override
	public ViewFactory getViewFactory() {
		return codeEditorViewFactory;
	}

	@Override
	public String getContentType() {
		return "text/csvator";
	}

}
