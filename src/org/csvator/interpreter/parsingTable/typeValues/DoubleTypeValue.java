package org.csvator.interpreter.parsingTable.typeValues;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.DoubleValue;
import org.csvator.interpreter.parsingTable.EmptyValue;
import org.csvator.interpreter.parsingTable.ValueInterface;

public class DoubleTypeValue implements TypeValueInterface {

	String id;

	public DoubleTypeValue(String id) {
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
		return "double";
	}

	@Override
	public Class getTypeClass() {
		return DoubleValue.class;
	}

}
