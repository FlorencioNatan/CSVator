package org.csvator.interpreter.parsingTable.typeValues;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.NullValue;
import org.csvator.interpreter.parsingTable.ValueInterface;

public class VoidTypeValue implements TypeValueInterface {

	private static VoidTypeValue instance;
	private String id;

	private VoidTypeValue() {
		this.id = "void";
	}

	public static VoidTypeValue getInstance() {
		if (VoidTypeValue.instance == null) {
			VoidTypeValue.instance = new VoidTypeValue();
		}
		return VoidTypeValue.instance;
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
		return "void";
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
		return NullValue.getInstace();
	}

}
