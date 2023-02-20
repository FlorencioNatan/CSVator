package org.csvator.interpreter.parsingTable.typeValues;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.IntegerValue;
import org.csvator.interpreter.parsingTable.ValueInterface;

public class IntTypeValue implements TypeValueInterface {

	private static IntTypeValue instance;
	private String id;

	private IntTypeValue() {
		this.id = "int";
	}

	public static IntTypeValue getInstance() {
		if (IntTypeValue.instance == null) {
			IntTypeValue.instance = new IntTypeValue();
		}
		return IntTypeValue.instance;
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
		return "int";
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
		return new IntegerValue(strValue, strValue);
	}

}
