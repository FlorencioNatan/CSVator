package org.csvator.interpreter.parsingTable.typeValues;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.ValueInterface;
import org.csvator.interpreter.parsingTable.VariableValue;

public class VariableTypeValue implements TypeValueInterface {

	private static VariableTypeValue instance;
	private String id;

	private VariableTypeValue() {
		this.id = "var";
	}

	public static VariableTypeValue getInstance() {
		if (VariableTypeValue.instance == null) {
			VariableTypeValue.instance = new VariableTypeValue();
		}
		return VariableTypeValue.instance;
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
		return "var";
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
		return new VariableValue(strValue);
	}

}
