package org.csvator.interpreter.parsingTable.typeValues;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.EmptyValue;
import org.csvator.interpreter.parsingTable.ValueInterface;

public class ListTypeValue implements TypeValueInterface {

	private static ListTypeValue instance;
	String id;

	public ListTypeValue() {
		this.id = "list";
	}

	public static ListTypeValue getInstace() {
		if (ListTypeValue.instance == null) {
			ListTypeValue.instance = new ListTypeValue();
		}
		return ListTypeValue.instance;
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
		return "list";
	}

	@Override
	public TypeValueInterface getType() {
		return this;
	}

}
