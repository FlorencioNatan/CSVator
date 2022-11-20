package org.csvator.interpreter.parsingTable.typeValues;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.EmptyValue;
import org.csvator.interpreter.parsingTable.ValueInterface;

public class BoolTypeValue implements TypeValueInterface {

	private static BoolTypeValue instance;
	String id;

	private BoolTypeValue() {
		this.id = "bool";
	}

	public static BoolTypeValue getInstace() {
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
		return new EmptyValue(this.id);
	}

	@Override
	public String toString() {
		return "bool";
	}

	@Override
	public TypeValueInterface getType() {
		return this;
	}

}
