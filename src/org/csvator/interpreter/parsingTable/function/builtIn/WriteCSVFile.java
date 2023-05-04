package org.csvator.interpreter.parsingTable.function.builtIn;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.CollectionValueInterface;
import org.csvator.interpreter.parsingTable.NullValue;
import org.csvator.interpreter.parsingTable.StringValue;
import org.csvator.interpreter.parsingTable.ValueInterface;
import org.csvator.interpreter.parsingTable.function.FunctionValueInterface;
import org.csvator.interpreter.parsingTable.function.InvalidNumberOfParametersException;
import org.csvator.interpreter.parsingTable.function.TypeMismatchException;
import org.csvator.interpreter.parsingTable.typeValues.AnyTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.BoolTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.FunctionTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.StringTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.TypeValueInterface;
import org.csvator.interpreter.parsingTable.typeValues.VoidTypeValue;

public class WriteCSVFile implements FunctionValueInterface {

	@Override
	public String getId() {
		return "writeCSVFile";
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
		if (values.size() < 2 || values.size() > 5) {
			throw new InvalidNumberOfParametersException("The function writeCSVFile expects between 2 and 5 parameters, but " + values.size() +  " are found.");
		}
		Environment local = new Environment();
		local.setFatherEnvironment(father);
		StringValue file = (StringValue) values.get(0).evaluate(father);
		CollectionValueInterface collection = (CollectionValueInterface) values.get(1).evaluate(father);

		local.putValue("file", file);
		local.putValue("collection", collection);
		
		if (values.size() >= 3) {
			StringValue separator = (StringValue) values.get(2).evaluate(father);
			local.putValue("separator", separator);
		}
		if (values.size() >= 4) {
			StringValue enclosure = (StringValue) values.get(3).evaluate(father);
			local.putValue("enclosure", enclosure);
		}
		if (values.size() == 5) {
			StringValue charset = (StringValue) values.get(4).evaluate(father);
			local.putValue("charset", charset);
		}
		return local;
	}

	@Override
	public TypeValueInterface getReturnType() {
		return VoidTypeValue.getInstance();
	}

	@SuppressWarnings("unused")
	@Override
	public ValueInterface apply(Environment env) {
		StringValue file = (StringValue) env.getValueOf("file");
		CollectionValueInterface collection = (CollectionValueInterface) env.getValueOf("collection");
		StringValue separator = new StringValue(",",",");
		StringValue enclosure = new StringValue("\"","\"");
		StringValue charset = new StringValue("UTF-8", "UTF-8");

		if (env.containsKey("enclosure")) {
			enclosure = (StringValue) env.getValueOf("enclosure");
		}

		if (env.containsKey("separator")) {
			separator = (StringValue) env.getValueOf("separator");
		}

		if (env.containsKey("charset")) {
			charset = (StringValue) env.getValueOf("charset");
		}

		StringBuilder content = new StringBuilder();

		Path path = Paths.get(file.getStrValue());
		try {
			BufferedWriter writer;
			writer = Files.newBufferedWriter(path, Charset.forName(charset.getStrValue()), StandardOpenOption.CREATE);
			String line = null;
			String[][] table = collection.buildTable();
			String[] header = collection.buildTableHeader();

			if (header != null) {
				content.append(String.join(separator.getStrValue(), header));
				content.append("\n");
			}

			for (String[] strings : table) {
				content.append(String.join(separator.getStrValue(), strings));
				content.append("\n");
			}
			writer.write(content.toString());
			writer.close();
		}catch(IOException ex){
			ex.printStackTrace();
		}

		return NullValue.getInstace();
	}

}
