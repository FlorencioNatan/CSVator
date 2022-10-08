package org.csvator.interpreter.parsingTable;

import org.csvator.interpreter.environment.Environment;

public class BooleanValue implements ValueInterface {

	String id;
	boolean value;

	public BooleanValue(String id, String strValue) {
		this.id = id;
		this.value = Boolean.valueOf(strValue);
	}

	public BooleanValue(String id, boolean value) {
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

	public boolean getBooleanValue(Environment env) {
		return this.value;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return Boolean.toString(value);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class getTypeClass() {
		return this.getClass();
	}

}
