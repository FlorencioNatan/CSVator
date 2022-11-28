package org.csvator.interpreter.parsingTable;

import java.util.HashMap;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.typeValues.DictTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.TypeValueInterface;

public class DictValue implements ValueInterface {

	private String id;
	private HashMap<ValueInterface, ValueInterface> value;

	public DictValue(String id) {
		this.id = id;
		this.value = new HashMap<ValueInterface, ValueInterface>();
	}

	public DictValue(String id, HashMap<ValueInterface, ValueInterface> value) {
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

	public HashMap<ValueInterface, ValueInterface> getVectorValue() {
		return value;
	}

	@Override
	public String toString() {
		return this.value.toString();
	}

	@Override
	public TypeValueInterface getType() {
		return DictTypeValue.getInstace();
	}

}
