package org.csvator.main.gui.CSVatorCodeEditor;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.PlainDocument;
import javax.swing.text.PlainView;
import javax.swing.text.Segment;
import javax.swing.text.Utilities;


public class CSVatorCodeEditorView extends PlainView {
	private static String COMMENTS          = "(//.*|#.*)";
	private static String GENERIC_KEYWORD   = "\\b(null|record|fields|type|invariant|invariants|union|end)\\b";
	private static String TYPES             = "\\b(int|bool|double|string|date|datetime|void|any)\\b";
	private static String NUMBER            = "(\\d+)";
	private static String FLOAT             = "(\\d+\\.\\\\d+)";
	private static String BOOLEANS          = "(true|false)";
	private static String FUNCTION_SYMBOLS  = "(:|->|\\||\\[|\\])";
	private static String DOLLAR_SYMBOL  = "(\\$)";
	private static String SYMBOL_OPERATORS  = "(\\+\\+|--|!!|!\\?|!<|!>|!#|\\+|-|\\*|[^/]/|[^-]>|>=|<|<=|=|<>|:=)";
	private static String KEYWORD_OPERATORS = "\\b(and|or|xor|not|implies|typeis|forall|exists|in)\\b";
	private static String COLLECTIONS       = "\\b(vector|dict|set|list)\\b";
	private static String FUNCTIONS         = "\\b(sort|swap|update|map|filter|reduce|printTable|readCSVFile|writeCSVFile|Date|DateTime|regex_match|regex_replace)\\b";
	private static String STRING            = "(\"(?:(?:\\\\\\\")|[^\"])*[^\\\\]\")";
	private static String IDENTIFIER        = "\\b([a-zA-Z]([a-zA-Z0-9_])*)\\b";

	private static LinkedList<PatternColor> patterns = new LinkedList<PatternColor>();
	private Color foregroundColor = new Color(171, 178, 191);

	static {
		patterns.add(new PatternColor(Pattern.compile(COMMENTS), new Color(92, 99, 112))); // Gray
		patterns.add(new PatternColor(Pattern.compile(GENERIC_KEYWORD), new Color(218, 100, 241))); // Purple
		patterns.add(new PatternColor(Pattern.compile(TYPES), new Color(229, 192, 123))); // Yellow
		patterns.add(new PatternColor(Pattern.compile(NUMBER), new Color(209, 154, 102))); // Dark Yellow
		patterns.add(new PatternColor(Pattern.compile(FLOAT), new Color(209, 154, 102))); // Dark Yellow
		patterns.add(new PatternColor(Pattern.compile(BOOLEANS), new Color(209, 154, 102))); // Dark Yellow
		patterns.add(new PatternColor(Pattern.compile(FUNCTION_SYMBOLS), new Color(86, 182, 194))); // Cyan
		patterns.add(new PatternColor(Pattern.compile(DOLLAR_SYMBOL), new Color(218, 100, 241))); // Purple
		patterns.add(new PatternColor(Pattern.compile(SYMBOL_OPERATORS), new Color(218, 100, 241))); // Purple
		patterns.add(new PatternColor(Pattern.compile(KEYWORD_OPERATORS), new Color(218, 100, 241))); // Purple
		patterns.add(new PatternColor(Pattern.compile(COLLECTIONS), new Color(97, 175, 239))); // Blue
		patterns.add(new PatternColor(Pattern.compile(FUNCTIONS), new Color(97, 175, 239))); // Blue
		patterns.add(new PatternColor(Pattern.compile(STRING), new Color(152, 195, 121))); // Green
		patterns.add(new PatternColor(Pattern.compile(IDENTIFIER), new Color(224, 108, 117))); // Red
	}

	public CSVatorCodeEditorView(Element elem) {
		super(elem);
		getDocument().putProperty(PlainDocument.tabSizeAttribute, 4);
	}

	@Override
	protected float drawUnselectedText(Graphics2D g, float x, float y, int p0, int p1) throws BadLocationException {
		Document doc = getDocument();
		String text = doc.getText(p0, p1 - p0);

		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

		Segment segment = getLineBuffer();

		LinkedList<IntervalColor> intervalColors = new LinkedList<IntervalColor>();

		//Match all regexes on this snippet, store positions
		for (PatternColor entry : patterns) {

			Matcher matcher = entry.pattern.matcher(text);

			while (matcher.find()) {
				intervalColors.add(new IntervalColor(matcher.start(1), matcher.end(), entry.color));
			}
		}

		intervalColors.sort(null);

		Iterator<IntervalColor> iterator = intervalColors.iterator();
		IntervalColor intervalColor = iterator.hasNext() ? (IntervalColor) iterator.next() : null;
		IntervalColor nextIC = null;
		while (iterator.hasNext()) {
			nextIC = (IntervalColor) iterator.next();

			if (intervalColor.end > nextIC.start) {
				iterator.remove();
				continue;
			}

			intervalColor = nextIC;
		}

		int i = 0;
		//Color the parts
		for (IntervalColor entry : intervalColors) {
			int start = entry.start;
			int end = entry.end;

			if (i < start) {
				g.setColor(foregroundColor);
				doc.getText(p0 + i, start - i, segment);
				x = Utilities.drawTabbedText(segment, x, y, g, this, i);
			}

			g.setColor(entry.color);
			i = end;
			doc.getText(p0 + start, i - start, segment);
			x = Utilities.drawTabbedText(segment, x, y, g, this, start);
		}

		// Paint possible remaining text black
		if (i < text.length()) {
			g.setColor(foregroundColor);
			doc.getText(p0 + i, text.length() - i, segment);
			x = Utilities.drawTabbedText(segment, x, y, g, this, i);
		}

		return x;
	}

	private static class PatternColor {
		Pattern pattern;
		Color color;

		public PatternColor(Pattern pattern, Color color) {
			this.pattern = pattern;
			this.color = color;
		}
	}

	class IntervalColor implements Comparable<IntervalColor> {
		int start;
		int end;
		Color color;

		public IntervalColor(int start, int end, Color color) {
			this.start = start;
			this.end = end;
			this.color = color;
		}

		@Override
		public int compareTo(IntervalColor o) {
			return this.start - o.start;
		}

	}

}
