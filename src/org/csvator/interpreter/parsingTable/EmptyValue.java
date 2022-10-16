package org.csvator.interpreter.parsingTable;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.typeValues.TypeValueInterface;
import org.csvator.interpreter.parsingTable.typeValues.VoidTypeValue;

public class EmptyValue implements ValueInterface {

	String id;

	public EmptyValue(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public ValueInterface evaluate(Environment env) {
		throw new NullPointerException("A variável " + id + " não foi definida.");
	}

	@Override
	public TypeValueInterface getType() {
		return VoidTypeValue.getInstace();
	}

}
