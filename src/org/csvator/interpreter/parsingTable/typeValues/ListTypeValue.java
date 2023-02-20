package org.csvator.interpreter.parsingTable.typeValues;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.ListValue;
import org.csvator.interpreter.parsingTable.ValueInterface;

public class ListTypeValue implements TypeValueInterface {

	private static ListTypeValue instance;
	private String id;

	public ListTypeValue() {
		this.id = "list";
	}

	public static ListTypeValue getInstance() {
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
		return this;
	}

	@Override
	public String toString() {
		return "list";
	}

	@Override
	public TypeValueInterface getType() {
		return this;
	}

	@Override
	public boolean equalsToType(TypeValueInterface type) {
		if (type == this) {
			return true;
		}

		if (type instanceof AnyTypeValue) {
			return true;
		}

		if (type instanceof CollectionTypeValue) {
			return true;
		}

		return false;
	}

	@Override
	public ValueInterface createValue(String strValue) {
		return new ListValue(strValue);
	}

}
