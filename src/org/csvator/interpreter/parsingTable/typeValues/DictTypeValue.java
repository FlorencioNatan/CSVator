package org.csvator.interpreter.parsingTable.typeValues;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.EmptyValue;
import org.csvator.interpreter.parsingTable.ValueInterface;

public class DictTypeValue implements TypeValueInterface {

	private static DictTypeValue instance;
	String id;

	public DictTypeValue() {
		this.id = "dict";
	}

	public static DictTypeValue getInstace() {
		if (DictTypeValue.instance == null) {
			DictTypeValue.instance = new DictTypeValue();
		}
		return DictTypeValue.instance;
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
		return "dict";
	}

	@Override
	public TypeValueInterface getType() {
		return this;
	}

}
