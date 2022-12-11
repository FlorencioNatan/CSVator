package org.csvator.interpreter.parsingTable;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.function.TypeMismatchException;
import org.csvator.interpreter.parsingTable.typeValues.IntTypeValue;
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
		return StringTypeValue.getInstance();
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

	@Override
	public void remove(ValueInterface value) {
		if (!(value instanceof IntegerValue)) {
			throw new TypeMismatchException("The index to be removed must of type " + IntTypeValue.getInstance());
		}
		int index = ((IntegerValue) value).value;
		this.value = this.value.substring(0, index) + this.value.substring(index + 1);
	}

	@Override
	public ValueInterface get(ValueInterface value) {
		if (!(value instanceof IntegerValue)) {
			throw new TypeMismatchException("The index must of type " + IntTypeValue.getInstance());
		}

		String character = String.valueOf(this.value.charAt(((IntegerValue) value).value));
		return new StringValue(character, character);
	}

	@Override
	public ValueInterface contains(ValueInterface value) {
		if (!(value instanceof StringValue)) {
			throw new TypeMismatchException("The value must of type " + StringTypeValue.getInstance());
		}
		if (this.value.contains(((StringValue) value).value)) {
			new BooleanValue("true", true);
		}
		return new BooleanValue("false", false);
	}

	@Override
	public ValueInterface head() {
		String newString = this.value.substring(0, 0);
		return new StringValue(newString, newString);
	}

	@Override
	public ValueInterface tail() {
		String newString = this.value.substring(1, this.value.length() - 1);
		return new StringValue(newString, newString);
	}

	@Override
	public ValueInterface size() {
		return new IntegerValue(Integer.toString(this.value.length()), this.value.length());
	}

	@Override
	public CollectionValueInterface update(ValueInterface index, ValueInterface value) {
		if (!(index instanceof IntegerValue)) {
			throw new TypeMismatchException("The index must of type " + IntTypeValue.getInstance());
		}

		if (!(value instanceof StringValue)) {
			throw new TypeMismatchException("The value must of type " + StringTypeValue.getInstance());
		}

		int intIndex = ((IntegerValue) index).value;
		String strValue = ((StringValue) value).value;

		this.value = this.value.substring(0, intIndex)
			+ strValue + this.value.substring(intIndex + 1);
		return this;
	}

}
