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

	public static IntTypeValue getInstace() {
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
		// TODO Auto-generated method stub
		return "int";
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class getTypeClass() {
		return this.getClass();
	}

}
