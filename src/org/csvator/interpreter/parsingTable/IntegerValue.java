package org.csvator.interpreter.parsingTable;

import org.csvator.interpreter.environment.Environment;

public class IntegerValue implements ValueInterface {

	String id;
	int value;

	public IntegerValue(String id, String strValue) {
		this.id = id;
		this.value = Integer.valueOf(strValue);
	}

	public IntegerValue(String id, int value) {
		this.id = id;
		this.value = value;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public ValueInterface evaluate(Environment env) {
		return this;
	}

	public int getIntValue(Environment env) {
		return this.value;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return Integer.toString(value);
	}

}
