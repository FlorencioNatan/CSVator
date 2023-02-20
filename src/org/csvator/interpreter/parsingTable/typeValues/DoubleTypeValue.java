package org.csvator.interpreter.parsingTable.typeValues;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.DoubleValue;
import org.csvator.interpreter.parsingTable.ValueInterface;

public class DoubleTypeValue implements TypeValueInterface {

	private static DoubleTypeValue instance;
	private String id;

	private DoubleTypeValue() {
		this.id = "double";
	}

	public static DoubleTypeValue getInstance() {
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
		return this;
	}

	@Override
	public String toString() {
		return "double";
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
		return new DoubleValue(strValue, strValue);
	}

}
