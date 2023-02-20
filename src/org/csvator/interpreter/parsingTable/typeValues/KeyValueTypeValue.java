package org.csvator.interpreter.parsingTable.typeValues;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.ValueInterface;

public class KeyValueTypeValue implements TypeValueInterface {

	private static KeyValueTypeValue instance;
	private String id;

	public KeyValueTypeValue() {
		this.id = "key -> value";
	}

	public static KeyValueTypeValue getInstance() {
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
		return this;
	}

	@Override
	public String toString() {
		return "key -> value";
	}

	@Override
	public TypeValueInterface getType() {
		return this;
	}

	@Override
	public boolean equalsToType(TypeValueInterface type) {
		if (type == this) {
			return true;
		}

		if (type instanceof AnyTypeValue) {
			return true;
		}

		return false;
	}

	@Override
	public ValueInterface createValue(String strValue) {
		return null;
	}

}
