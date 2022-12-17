package org.csvator.interpreter.parsingTable;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.typeValues.TypeValueInterface;

public class NullaryExpressionValue implements ExpressionValueInterface {

	private String id;
	private ValueInterface value;

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

	@Override
	public String toString() {
		return this.value.toString();
	}

	@Override
	public int hashCode() {
		return this.value.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return this.value.equals(obj);
	}

	@Override
	public TypeValueInterface getType() {
		return this.value.getType();
	}

}
