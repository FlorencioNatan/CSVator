package org.csvator.interpreter.parsingTable;

import org.csvator.interpreter.environment.Environment;

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
	public Class getTypeClass() {
		return this.getClass();
	}

}
