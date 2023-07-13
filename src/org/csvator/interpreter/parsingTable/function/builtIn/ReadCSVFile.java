package org.csvator.interpreter.parsingTable.function.builtIn;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.csvator.helpers.RFC4180Parser;
import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.BooleanValue;
import org.csvator.interpreter.parsingTable.FieldValue;
import org.csvator.interpreter.parsingTable.RecordValue;
import org.csvator.interpreter.parsingTable.StringValue;
import org.csvator.interpreter.parsingTable.ValueInterface;
import org.csvator.interpreter.parsingTable.VectorValue;
import org.csvator.interpreter.parsingTable.function.FunctionValueInterface;
import org.csvator.interpreter.parsingTable.function.InvalidNumberOfParametersException;
import org.csvator.interpreter.parsingTable.function.TypeMismatchException;
import org.csvator.interpreter.parsingTable.typeValues.AnyTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.BoolTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.CollectionTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.FunctionTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.RecordTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.StringTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.TypeValueInterface;

public class ReadCSVFile implements FunctionValueInterface {

	@Override
	public String getId() {
		return "readCSVFile";
	}

	@Override
	public ValueInterface evaluate(Environment env) {
		return this;
	}

	@Override
	public TypeValueInterface getType() {
		LinkedList<TypeValueInterface> parametersType = new LinkedList<>();
		parametersType.add(StringTypeValue.getInstance());
		parametersType.add(AnyTypeValue.getInstance());
		parametersType.add(StringTypeValue.getInstance());
		parametersType.add(StringTypeValue.getInstance());
		parametersType.add(BoolTypeValue.getInstance());
		parametersType.add(StringTypeValue.getInstance());
		return new FunctionTypeValue(parametersType, getReturnType());
	}

	public Environment createLocalEnvironment(LinkedList<ValueInterface> values, Environment father) throws TypeMismatchException {
		if (values.size() < 2 || values.size() > 6) {
			throw new InvalidNumberOfParametersException("The function readCSVFile expects between 2 and 6 parameters, but " + values.size() +  " are found.");
		}
		Environment local = new Environment();
		local.setFatherEnvironment(father);
		StringValue file = (StringValue) values.get(0).evaluate(father);
		RecordTypeValue record = (RecordTypeValue) values.get(1).evaluate(father);

		local.putValue("file", file);
		local.putValue("record", record);
		
		if (values.size() >= 3) {
			StringValue separator = (StringValue) values.get(2).evaluate(father);
			local.putValue("separator", separator);
		}
		if (values.size() >= 4) {
			StringValue enclosure = (StringValue) values.get(3).evaluate(father);
			local.putValue("enclosure", enclosure);
		}
		if (values.size() >= 5) {
			BooleanValue ignoreFirstLine = (BooleanValue) values.get(4).evaluate(father);
			local.putValue("ignoreFirstLine", ignoreFirstLine);
		}
		if (values.size() == 6) {
			StringValue charset = (StringValue) values.get(5).evaluate(father);
			local.putValue("charset", charset);
		}
		return local;
	}

	@Override
	public TypeValueInterface getReturnType() {
		return CollectionTypeValue.getInstance();
	}

	@SuppressWarnings("unused")
	@Override
	public ValueInterface apply(Environment env) {
		StringValue file = (StringValue) env.getValueOf("file");
		RecordTypeValue record = (RecordTypeValue) env.getValueOf("record");
		StringValue separator = new StringValue(",");
		StringValue enclosure = new StringValue("\"");
		BooleanValue ignoreFirstLine = new BooleanValue("true", true);
		StringValue charset = new StringValue("UTF-8");

		if (env.containsKey("enclosure")) {
			enclosure = (StringValue) env.getValueOf("enclosure");
		}

		if (env.containsKey("separator")) {
			separator = (StringValue) env.getValueOf("separator");
		}

		if (env.containsKey("ignoreFirstLine")) {
			ignoreFirstLine = (BooleanValue) env.getValueOf("ignoreFirstLine");
		}

		if (env.containsKey("charset")) {
			charset = (StringValue) env.getValueOf("charset");
		}

		VectorValue vector = new VectorValue();

		RFC4180Parser parser = new RFC4180Parser(
			enclosure.getStrValue(),
			separator.getStrValue(),
			ignoreFirstLine.getBooleanValue(),
			charset.getStrValue()
		);
		List<List<String>> parsedTable = parser.parse(file.getStrValue());
		for (List<String> line: parsedTable) {
			vector.concatAtTail(this.createRecord(record, line));
		}

		return vector;
	}

	private RecordValue createRecord(RecordTypeValue recordType, List<String> lineData) {
		LinkedList<FieldValue> fieldList = recordType.getFields();
		ValueInterface[] arguments = new ValueInterface[lineData.size()];
		Iterator<FieldValue> fieldIterator = fieldList.iterator();
		for(int i = 0; i < lineData.size(); i++) {
			FieldValue field = fieldIterator.next();
			arguments[i] = field.getType().createValue(lineData.get(i));
		}

		return (RecordValue) recordType.apply(arguments);
	}

}
