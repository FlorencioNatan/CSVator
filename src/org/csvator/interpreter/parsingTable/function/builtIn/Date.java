package org.csvator.interpreter.parsingTable.function.builtIn;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.DateValue;
import org.csvator.interpreter.parsingTable.StringValue;
import org.csvator.interpreter.parsingTable.ValueInterface;
import org.csvator.interpreter.parsingTable.function.FunctionValueInterface;
import org.csvator.interpreter.parsingTable.function.InvalidNumberOfParametersException;
import org.csvator.interpreter.parsingTable.function.TypeMismatchException;
import org.csvator.interpreter.parsingTable.typeValues.DateTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.FunctionTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.StringTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.TypeValueInterface;

public class Date implements FunctionValueInterface {

	@Override
	public String getId() {
		return "Date";
	}

	@Override
	public ValueInterface evaluate(Environment env) {
		return this;
	}

	@Override
	public TypeValueInterface getType() {
		LinkedList<TypeValueInterface> parametersType = new LinkedList<>();
		parametersType.add(StringTypeValue.getInstance());
		parametersType.add(StringTypeValue.getInstance());
		return new FunctionTypeValue(parametersType, getReturnType());
	}

	public Environment createLocalEnvironment(LinkedList<ValueInterface> values, Environment father) throws TypeMismatchException {
		if (values.size() > 2) {
			throw new InvalidNumberOfParametersException("The function date expects between 1 and 2 parameters, but " + values.size() +  " are found.");
		}
		Environment local = new Environment();
		local.setFatherEnvironment(father);

		if (values.size() >= 1) {
			StringValue date = (StringValue) values.get(0).evaluate(father);
			local.putValue("dtTime", date);
		}
		if (values.size() == 2) {
			StringValue format = (StringValue) values.get(1).evaluate(father);
			local.putValue("format", format);
		}

		return local;
	}

	@Override
	public TypeValueInterface getReturnType() {
		return DateTypeValue.getInstance();
	}

	@SuppressWarnings("unused")
	@Override
	public ValueInterface apply(Environment env) {
		if (!env.containsKey("dtTime")) {
			return new DateValue("now", LocalDate.now());
		}

		StringValue format = new StringValue("","yyyy-MM-dd");
		if (env.containsKey("format")) {
			format = (StringValue) env.getValueOf("format");
		}

		StringValue strDate = (StringValue) env.getValueOf("dtTime");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format.getStrValue());
		LocalDate date = LocalDate.parse(strDate.getStrValue(), formatter);

		return new DateValue(strDate.getStrValue(), date);
	}

}
