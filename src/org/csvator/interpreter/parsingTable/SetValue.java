package org.csvator.interpreter.parsingTable;


import java.util.HashSet;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.typeValues.SetTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.TypeValueInterface;

public class SetValue implements ValueInterface {

	private String id;
	private HashSet<ValueInterface> value;

	public SetValue(String id) {
		this.id = id;
		this.value = new HashSet<ValueInterface>();
	}

	public SetValue(String id, HashSet<ValueInterface> value) {
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

	public HashSet<ValueInterface> getSetValue() {
		return value;
	}

	@Override
	public String toString() {
		return this.value.toString();
	}

	@Override
	public TypeValueInterface getType() {
		return SetTypeValue.getInstace();
	}

}
