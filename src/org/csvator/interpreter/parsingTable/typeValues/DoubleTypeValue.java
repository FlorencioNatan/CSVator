package org.csvator.interpreter.parsingTable.typeValues;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.EmptyValue;
import org.csvator.interpreter.parsingTable.ValueInterface;

public class DoubleTypeValue implements TypeValueInterface {

	private static DoubleTypeValue instance;
	String id;

	private DoubleTypeValue() {
		this.id = "double";
	}

	public static DoubleTypeValue getInstace() {
		if (DoubleTypeValue.instance == null) {
			DoubleTypeValue.instance = new DoubleTypeValue();
		}
		return DoubleTypeValue.instance;
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
		return "double";
	}

	@Override
	public TypeValueInterface getType() {
		return this;
	}

}
