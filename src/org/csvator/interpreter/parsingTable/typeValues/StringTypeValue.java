package org.csvator.interpreter.parsingTable.typeValues;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.StringValue;
import org.csvator.interpreter.parsingTable.ValueInterface;

public class StringTypeValue implements TypeValueInterface {

	private static StringTypeValue instance;
	private String id;

	public StringTypeValue() {
		this.id = "string";
	}

	public static StringTypeValue getInstance() {
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
		return this;
	}

	@Override
	public String toString() {
		return "string";
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

		if (type instanceof CollectionTypeValue) {
			return true;
		}

		return false;
	}

	@Override
	public ValueInterface createValue(String strValue) {
		return new StringValue(strValue);
	}

}
