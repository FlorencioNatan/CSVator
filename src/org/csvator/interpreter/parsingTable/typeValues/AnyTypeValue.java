package org.csvator.interpreter.parsingTable.typeValues;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.ValueInterface;

public class AnyTypeValue implements TypeValueInterface {

	private static AnyTypeValue instance;
	private String id;

	private AnyTypeValue() {
		this.id = "any";
	}

	public static AnyTypeValue getInstance() {
		if (AnyTypeValue.instance == null) {
			AnyTypeValue.instance = new AnyTypeValue();
		}
		return AnyTypeValue.instance;
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
		return "any";
	}

	@Override
	public TypeValueInterface getType() {
		return this;
	}

	@Override
	public boolean equalsToType(TypeValueInterface type) {
		return true;
	}

	@Override
	public ValueInterface createValue(String strValue) {
		return null;
	}

}
