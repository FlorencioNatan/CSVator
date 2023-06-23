package org.csvator.interpreter.parsingTable.typeValues;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.SetValue;
import org.csvator.interpreter.parsingTable.ValueInterface;

public class SetTypeValue implements TypeValueInterface {

	private static SetTypeValue instance;
	private String id;

	public SetTypeValue() {
		this.id = "set";
	}

	public static SetTypeValue getInstance() {
		if (SetTypeValue.instance == null) {
			SetTypeValue.instance = new SetTypeValue();
		}
		return SetTypeValue.instance;
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
		return "set";
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
		return new SetValue();
	}


}
