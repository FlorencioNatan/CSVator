package org.csvator.interpreter.parsingTable;

import org.csvator.interpreter.environment.Environment;

public class ArgumentValue implements ValueInterface {

	String id;
	String idVariable;

	public ArgumentValue(String id, String idVariable) {
		this.id = id;
		this.idVariable = idVariable;
	}

	public String getIdVariable() {
		return idVariable;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public ValueInterface evaluate(Environment env) {
		return env.getValueOf(idVariable).evaluate(env);
	}

}
