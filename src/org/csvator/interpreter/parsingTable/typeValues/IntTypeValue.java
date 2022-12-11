package org.csvator.interpreter.parsingTable.typeValues;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.EmptyValue;
import org.csvator.interpreter.parsingTable.ValueInterface;

public class IntTypeValue implements TypeValueInterface {

	private static IntTypeValue instance;
	String id;

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
		return new EmptyValue(this.id);
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

}
