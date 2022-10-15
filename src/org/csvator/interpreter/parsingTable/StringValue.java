package org.csvator.interpreter.parsingTable;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.typeValues.StringTypeValue;

public class StringValue implements ValueInterface {

	String id;
	String value;

	public StringValue(String id, String strValue) {
		this.id = id;
		this.value = strValue;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public ValueInterface evaluate(Environment env) {
		return this;
	}

	public String getStrValue(Environment env) {
		return this.value;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.value;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class getTypeClass() {
		return StringTypeValue.class;
	}

}
