package org.csvator.main.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidParameterException;
import java.text.Normalizer;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.csvator.helpers.RFC4180Parser;

public class CSVatorInference {

	private String filePath;
	private String charset;
	private String separator;
	private String delimiter;
	private boolean ignoreFirstLine;
	private boolean useWholeFile;
	private int useNLines;

	private static int NONE = -1;
	private static int INT = 0;
	private static int DOUBLE = 1;
	private static int VOID = 2;
	private static int BOOLEAN = 3;
	private static int DATE = 4;
	private static int DATETIME = 5;
	private static int STRING = 6;

	public CSVatorInference(String filePath, String charset, String separator, String delimiter, boolean ignoreFirstLine) {
		if (filePath.equals("")) {
			throw new InvalidParameterException("The file path must be informed.");
		}
		if (charset.equals("")) {
			throw new InvalidParameterException("The charset must be informed.");
		}
		if (separator.equals("")) {
			throw new InvalidParameterException("The separator must be informed.");
		}
		if (delimiter.equals("")) {
			throw new InvalidParameterException("The delimiter must be informed.");
		}
		this.filePath = filePath;
		this.charset = charset;
		this.separator = separator;
		this.delimiter = delimiter;
		this.ignoreFirstLine = ignoreFirstLine;
		useWholeFile = true;
		useNLines = 0;
	}

	public void useWholeFile() {
		useWholeFile = true;
		useNLines = 0;
	}

	public void useNLines(int n) {
		useNLines = n;
	}

	public String inferFromCSVFile() {
		StringBuilder content = new StringBuilder();
		content.append("type importedRecord := record\n");
		content.append("\tfields\n");
		String[] fields = this.processFields();
		for (String field : fields) {
			content.append("\t\t" + field + ",\n");
		}
		content.append("\tend\n");
		content.append("end;\n\n");
		content.append("content := readCSVFile(\"" + this.filePath + "\", importedRecord);\n\n");
		content.append("printTable(content);\n");
		return content.toString();
	}

	private String[] processFields() {
		String[] fieldNames = null;
		String[] fieldTypes = null;
		boolean firstLine = true;
		int lineNumber = 0;

		RFC4180Parser parser = new RFC4180Parser(
			delimiter,
			separator,
			false,
			charset
		);
		List<List<String>> parsedTable = parser.parse(filePath);

		for (List<String> line: parsedTable) {
			if (firstLine && ignoreFirstLine) {
				firstLine = false;
				fieldNames = new String[line.size()];
				int i = 0;
				for (String fieldName : line) {
					fieldNames[i] = fieldName.replaceAll("\\s", "_");
					fieldNames[i] = Normalizer.normalize(fieldNames[i], Normalizer.Form.NFKD).replaceAll("\\p{M}", "");
					i++;
				}
				continue;
			} else if (firstLine) {
				fieldNames = new String[line.size()];
				for (int i = 0; i < fieldNames.length; i++) {
					fieldNames[i] = "Col_" + i;
				}
			}

			if (!useWholeFile && lineNumber < useNLines) {
				break;
			}

			String[] lineData = new String[line.size()];
			int i = 0;
			for (String dataValue : line) {
				lineData[i] = dataValue;
				i++;
			}
			if (fieldTypes == null) {
				fieldTypes = new String[lineData.length];
			}
			fieldTypes = inferTypes(lineData, fieldTypes);
			lineNumber++;
		}

		return concatFieldTypeAndName(fieldNames, fieldTypes);
	}

	private String[] inferTypes(String[] lineData, String[] fieldTypes) {
		String[] newFieldTypes = new String[lineData.length];
		for (int i = 0; i < lineData.length; i++) {
			newFieldTypes[i] = inferFieldType(lineData[i], fieldTypes[i]);
		}
		return newFieldTypes;
	}

	private String inferFieldType(String data, String type) {
		String typeOfData = this.typeOfData(data);
		int precedenceOfData = precedenceOfType(typeOfData);
		int precedenceOfCurrentType = precedenceOfType(type);
		if (precedenceOfCurrentType > precedenceOfData && areSimilarTypes(typeOfData, type)) {
			return type;
		} else if (areSimilarTypes(typeOfData, type)) {
			return typeOfData;
		} else {
			return "string";
		}
	}

	private boolean areSimilarTypes(String typeA, String typeB) {
		int precedenceTypeA = precedenceOfType(typeA);
		int precedenceTypeB = precedenceOfType(typeB);

		if (precedenceTypeA >= -1 && precedenceTypeA <= 1 &&
			precedenceTypeB >= -1 && precedenceTypeB <= 1
		) {
			return true;
		}

		if ((precedenceTypeA >= 4 && precedenceTypeA <= 5 || precedenceTypeA == -1) &&
			(precedenceTypeB >= 4 && precedenceTypeB <= 4 || precedenceTypeB == -1)
		) {
			return true;
		}

		return false;
	}

	private String typeOfData(String data) {
		Pattern regex;
		Matcher matcher;
		regex = Pattern.compile("\\d+\\.\\d+");
		matcher = regex.matcher(data);
		if (matcher.find()) {
			return "double";
		}

		regex = Pattern.compile("\\d+");
		matcher = regex.matcher(data);
		if (matcher.find()) {
			return "int";
		}

		regex = Pattern.compile("null");
		matcher = regex.matcher(data);
		if (matcher.find()) {
			return "void";
		}

		regex = Pattern.compile("true");
		matcher = regex.matcher(data);
		if (matcher.find()) {
			return "boolean";
		}

		regex = Pattern.compile("false");
		matcher = regex.matcher(data);
		if (matcher.find()) {
			return "boolean";
		}

		regex = Pattern.compile("\\d{4}-\\d{2}-\\d{2}T?\\d{2}:\\d{2}:\\d{2}");
		matcher = regex.matcher(data);
		if (matcher.find()) {
			return "datetime";
		}
		regex = Pattern.compile("\\d{2}-\\d{2}-\\d{4}T?\\d{2}:\\d{2}:\\d{2}");
		matcher = regex.matcher(data);
		if (matcher.find()) {
			return "datetime";
		}
		regex = Pattern.compile("\\d{2}/\\d{2}/\\d{4}T?\\d{2}:\\d{2}:\\d{2}");
		matcher = regex.matcher(data);
		if (matcher.find()) {
			return "datetime";
		}

		regex = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
		matcher = regex.matcher(data);
		if (matcher.find()) {
			return "date";
		}
		regex = Pattern.compile("\\d{2}-\\d{2}-\\d{4}");
		matcher = regex.matcher(data);
		if (matcher.find()) {
			return "date";
		}
		regex = Pattern.compile("\\d{2}/\\d{2}/\\d{4}");
		matcher = regex.matcher(data);
		if (matcher.find()) {
			return "date";
		}

		return "string";
	}

	private int precedenceOfType(String type) {
		if (type == null) {
			return NONE;
		}
		switch(type) {
			case "int":
				return INT;
			case "double":
				return DOUBLE;
			case "void":
				return VOID;
			case "boolean":
				return BOOLEAN;
			case "date":
				return DATE;
			case "datetime":
				return DATETIME;
			case "string":
				return STRING;
			default:
				return STRING;
		}
	}

	private String[] concatFieldTypeAndName(String[] fieldNames, String[] fieldTypes) {
		String[] concatField = new String[fieldNames.length];
		for (int i = 0; i < fieldNames.length; i++) {
			concatField[i] = fieldTypes[i] + " " + fieldNames[i];
		}
		return concatField;
	}

}
