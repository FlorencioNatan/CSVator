package org.csvator.interpreter.parsingTable.typeValues;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.EmptyValue;
import org.csvator.interpreter.parsingTable.ValueInterface;

public class StringTypeValue implements TypeValueInterface {

	private static StringTypeValue instance;
	String id;

	public StringTypeValue() {
		this.id = "string";
	}

	public static StringTypeValue getInstace() {
		if (StringTypeValue.instance == null) {
			StringTypeValue.instance = new StringTypeValue();
		}
		return StringTypeValue.instance;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public ValueInterface evaluate(Environment env) {
		return new EmptyValue(this.id);
	}

	@Override
	public String toString() {
		return "string";
	}

	@Override
	public TypeValueInterface getType() {
		return this;
	}

}
