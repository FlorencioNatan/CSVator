package org.csvator.interpreter.parsingTable.typeValues;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.StringValue;
import org.csvator.interpreter.parsingTable.ValueInterface;
import org.csvator.interpreter.parsingTable.function.builtIn.DateTime;

public class DateTimeTypeValue implements TypeValueInterface {

	private static DateTimeTypeValue instance;
	private String id;

	private DateTimeTypeValue() {
		this.id = "datetime";
	}

	public static DateTimeTypeValue getInstance() {
		if (DateTimeTypeValue.instance == null) {
			DateTimeTypeValue.instance = new DateTimeTypeValue();
		}
		return DateTimeTypeValue.instance;
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
		return "datetime";
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
		DateTime dateTime = new DateTime();
		StringValue value = new StringValue(strValue, strValue);

		return dateTime.apply(value);
	}

}
