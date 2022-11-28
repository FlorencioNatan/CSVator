package org.csvator.interpreter.parsingTable.typeValues;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.EmptyValue;
import org.csvator.interpreter.parsingTable.ValueInterface;

public class KeyValueTypeValue implements TypeValueInterface {

	private static KeyValueTypeValue instance;
	String id;

	public KeyValueTypeValue() {
		this.id = "key -> value";
	}

	public static KeyValueTypeValue getInstace() {
		if (KeyValueTypeValue.instance == null) {
			KeyValueTypeValue.instance = new KeyValueTypeValue();
		}
		return KeyValueTypeValue.instance;
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
		return "key -> value";
	}

	@Override
	public TypeValueInterface getType() {
		return this;
	}

}
