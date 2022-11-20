package org.csvator.interpreter.parsingTable.typeValues;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.EmptyValue;
import org.csvator.interpreter.parsingTable.ValueInterface;

public class AnyTypeValue implements TypeValueInterface {

	private static AnyTypeValue instance;
	String id;

	private AnyTypeValue() {
		this.id = "any";
	}

	public static AnyTypeValue getInstace() {
		if (AnyTypeValue.instance == null) {
			AnyTypeValue.instance = new AnyTypeValue();
		}
		return AnyTypeValue.instance;
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
		return "any";
	}

	@Override
	public TypeValueInterface getType() {
		return this;
	}

}
