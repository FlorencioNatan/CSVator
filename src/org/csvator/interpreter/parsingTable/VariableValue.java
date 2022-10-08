package org.csvator.interpreter.parsingTable;

import org.csvator.interpreter.environment.Environment;

public class VariableValue implements ValueInterface {

	String id;
	ValueInterface value;

	public VariableValue(String id, ValueInterface value) {
		this.id = id;
		this.value = value;
	}

	public VariableValue(String id) {
		this.id = id;
		this.value = null;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public ValueInterface evaluate(Environment env) {
		return env.getValueOf(id).evaluate(env);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class getTypeClass() {
		if (this.value != null) {
			return this.value.getTypeClass();
		}
		return this.getClass();
	}

}
