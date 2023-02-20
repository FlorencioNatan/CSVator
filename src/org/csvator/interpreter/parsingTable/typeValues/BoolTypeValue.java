package org.csvator.interpreter.parsingTable.typeValues;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.BooleanValue;
import org.csvator.interpreter.parsingTable.ValueInterface;

public class BoolTypeValue implements TypeValueInterface {

	private static BoolTypeValue instance;
	private String id;

	private BoolTypeValue() {
		this.id = "bool";
	}

	public static BoolTypeValue getInstance() {
		if (BoolTypeValue.instance == null) {
			BoolTypeValue.instance = new BoolTypeValue();
		}
		return BoolTypeValue.instance;
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
		return "bool";
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

		return false;
	}

	@Override
	public ValueInterface createValue(String strValue) {
		return new BooleanValue(strValue, strValue);
	}

}
