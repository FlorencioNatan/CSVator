package org.csvator.interpreter.parsingTable.function.builtIn;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.StringValue;
import org.csvator.interpreter.parsingTable.ValueInterface;
import org.csvator.interpreter.parsingTable.function.FunctionValueInterface;
import org.csvator.interpreter.parsingTable.function.InvalidNumberOfParametersException;
import org.csvator.interpreter.parsingTable.function.TypeMismatchException;
import org.csvator.interpreter.parsingTable.typeValues.AnyFunctionTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.CollectionTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.FunctionTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.StringTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.TypeValueInterface;

public class RegexReplace implements FunctionValueInterface {

	@Override
	public String getId() {
		return "regex_replace";
	}

	@Override
	public ValueInterface evaluate(Environment env) {
		return this;
	}

	@Override
	public TypeValueInterface getType() {
		LinkedList<TypeValueInterface> parametersType = new LinkedList<>();
		parametersType.add(CollectionTypeValue.getInstance());
		parametersType.add(AnyFunctionTypeValue.getInstance());
		return new FunctionTypeValue(parametersType, getReturnType());
	}

	public Environment createLocalEnvironment(LinkedList<ValueInterface> values, Environment father) throws TypeMismatchException {
		if (values.size() != 3 && values.size() != 4) {
			throw new InvalidNumberOfParametersException("The function map expects 3 or 4 parameters, but " + values.size() +  " are found.");
		}
		Environment local = new Environment();
		local.setFatherEnvironment(father);
		StringValue text = (StringValue) values.get(0).evaluate(father);
		StringValue regex = (StringValue) values.get(1).evaluate(father);
		StringValue replace = (StringValue) values.get(2).evaluate(father);
		local.putValue("text", text);
		local.putValue("regex", regex);
		local.putValue("replace", replace);

		if (values.size() == 4) {
			StringValue flags = (StringValue) values.get(3).evaluate(father);
			local.putValue("flags", flags);
		}

		return local;
	}

	@Override
	public TypeValueInterface getReturnType() {
		return StringTypeValue.getInstance();
	}

	@Override
	public ValueInterface apply(Environment env) {
		StringValue text = (StringValue) env.getValueOf("text");
		StringValue regex = (StringValue) env.getValueOf("regex");
		StringValue replace = (StringValue) env.getValueOf("replace");
		String flags = "";

		if (env.containsKey("flags")) {
			flags = ((StringValue) env.getValueOf("flags")).getStrValue();
		}

		String strText = text.getStrValue();
		Pattern pattern = Pattern.compile(regex.getStrValue(), parseFlags(flags));
		Matcher matcher = pattern.matcher(strText);
		String result = matcher.replaceAll(replace.getStrValue());

		return new StringValue(result);
	}

	private int parseFlags(String flags) {
		int flagsSetted = 0;

		for(char ch : flags.toCharArray()) {
			switch (ch) {
			case 'i':
				flagsSetted = flagsSetted | Pattern.CASE_INSENSITIVE;
				break;
			case 'u':
				flagsSetted = flagsSetted | Pattern.UNICODE_CASE | Pattern.UNICODE_CHARACTER_CLASS;
				break;
			case 'm':
				flagsSetted = flagsSetted | Pattern.MULTILINE;
				break;
			case 's':
				flagsSetted = flagsSetted | Pattern.DOTALL;
				break;
			case 'x':
				flagsSetted = flagsSetted | Pattern.COMMENTS;
				break;
			}
		}

		return flagsSetted;
	}

}
