package org.csvator.interpreter.parsingTable;

import org.csvator.interpreter.environment.Environment;

public class NullaryExpressionValue implements ExpressionValueInterface {

	String id;
	ValueInterface value;

	public NullaryExpressionValue(String id, ValueInterface value) {
		this.id = id;
		this.value = value;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public ValueInterface evaluate(Environment env) {
		return value.evaluate(env);
	}

}
