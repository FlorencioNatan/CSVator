package org.csvator.interpreter.parsingTable.typeValues;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.EmptyValue;
import org.csvator.interpreter.parsingTable.ValueInterface;

public class VoidTypeValue implements TypeValueInterface {

	String id;

	public VoidTypeValue(String id) {
		this.id = id;
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
		return "string";
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class getTypeClass() {
		return this.getClass();
	}

}
