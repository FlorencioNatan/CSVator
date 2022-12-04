package org.csvator.interpreter.parsingTable;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.function.TypeMismatchException;
import org.csvator.interpreter.parsingTable.typeValues.StringTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.TypeValueInterface;

public class StringValue implements CollectionValueInterface {

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
		return this.value;
	}

	@Override
	public TypeValueInterface getType() {
		return StringTypeValue.getInstace();
	}

	@Override
	public CollectionValueInterface concatAtHead(ValueInterface value) {
		if (value instanceof StringValue) {
			StringValue strVal = (StringValue) value;
			String result = strVal.value + this.value;
			return new StringValue(result, result);
		}

		throw new TypeMismatchException("It's not possible to concatenate a string with a " + value.getType());
	}

	@Override
	public CollectionValueInterface concatAtTail(ValueInterface value) {
		if (value instanceof StringValue) {
			StringValue strVal = (StringValue) value;
			String result = this.value + strVal.value;
			return new StringValue(result, result);
		}

		throw new TypeMismatchException("It's not possible to concatenate a string with a " + value.getType());
	}

	@Override
	public int hashCode() {
		int hash = this.value.hashCode();
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof StringValue)) {
			return false;
		}
		return this.value.equals(((StringValue) obj).value);
	}

}
