package org.csvator.interpreter.parsingTable;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.typeValues.TypeValueInterface;
import org.csvator.interpreter.parsingTable.typeValues.VoidTypeValue;

public class NullValue implements ValueInterface {

	private static NullValue instance;
	String id;

	private NullValue() {
		this.id = "null";
	}

	public static NullValue getInstace() {
		if (NullValue.instance == null) {
			NullValue.instance = new NullValue();
		}
		return NullValue.instance;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public ValueInterface evaluate(Environment env) {
		return this;
	}

	@Override
	public String toString() {
		return "null";
	}

	@Override
	public TypeValueInterface getType() {
		return VoidTypeValue.getInstace();
	}

}
