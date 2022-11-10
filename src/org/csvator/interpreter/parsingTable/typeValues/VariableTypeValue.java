package org.csvator.interpreter.parsingTable.typeValues;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.EmptyValue;
import org.csvator.interpreter.parsingTable.ValueInterface;

public class VariableTypeValue implements TypeValueInterface {

	private static VariableTypeValue instance;
	String id;

	private VariableTypeValue() {
		this.id = "var";
	}

	public static VariableTypeValue getInstace() {
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
		return new EmptyValue(this.id);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "var";
	}

	@Override
	public TypeValueInterface getType() {
		return this;
	}

}