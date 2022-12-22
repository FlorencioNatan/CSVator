package org.csvator.interpreter.parsingTable.typeValues;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.EmptyValue;
import org.csvator.interpreter.parsingTable.ValueInterface;

public class AnyFunctionTypeValue implements TypeValueInterface {

	private static AnyFunctionTypeValue instance;
	private String id;


	public static AnyFunctionTypeValue getInstance() {
		if (AnyFunctionTypeValue.instance == null) {
			AnyFunctionTypeValue.instance = new AnyFunctionTypeValue();
		}
		return AnyFunctionTypeValue.instance;
	}

	private AnyFunctionTypeValue() {
		this.id = "any function";
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
		return this.id;
	}

	@Override
	public TypeValueInterface getType() {
		return this;
	}

	@Override
	public boolean equalsToType(TypeValueInterface type) {
		if (type instanceof AnyTypeValue) {
			return true;
		}

		if (type instanceof AnyFunctionTypeValue) {
			return true;
		}

		if (type instanceof FunctionTypeValue) {
			return true;
		}

		return false;
	}

	@Override
	public ValueInterface createValue(String strValue) {
		return null;
	}

}
