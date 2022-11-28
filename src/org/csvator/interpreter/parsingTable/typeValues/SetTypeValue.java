package org.csvator.interpreter.parsingTable.typeValues;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.EmptyValue;
import org.csvator.interpreter.parsingTable.ValueInterface;

public class SetTypeValue implements TypeValueInterface {

	private static SetTypeValue instance;
	String id;

	public SetTypeValue() {
		this.id = "set";
	}

	public static SetTypeValue getInstace() {
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
		return new EmptyValue(this.id);
	}

	@Override
	public String toString() {
		return "set";
	}

	@Override
	public TypeValueInterface getType() {
		return this;
	}

}
