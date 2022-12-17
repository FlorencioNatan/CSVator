package org.csvator.interpreter.parsingTable;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.typeValues.KeyValueTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.TypeValueInterface;

public class KeyValueExpressionValue implements ExpressionValueInterface {

	private String id;
	private ValueInterface key;
	private ValueInterface value;

	public KeyValueExpressionValue(String id, ValueInterface key, ValueInterface value) {
		this.id = id;
		this.key = key;
		this.value = value;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public ValueInterface evaluate(Environment env) {
		return new KeyValueExpressionValue(id, key.evaluate(env), value.evaluate(env));
	}

	public ValueInterface getKey() {
		return key;
	}

	public ValueInterface getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "( "+ this.key.toString() + " -> " + this.value.toString() + " )";
	}

	@Override
	public TypeValueInterface getType() {
		return KeyValueTypeValue.getInstance();
	}

}
