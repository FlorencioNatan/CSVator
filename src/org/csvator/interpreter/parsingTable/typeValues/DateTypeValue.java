package org.csvator.interpreter.parsingTable.typeValues;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.StringValue;
import org.csvator.interpreter.parsingTable.ValueInterface;
import org.csvator.interpreter.parsingTable.function.builtIn.Date;

public class DateTypeValue implements TypeValueInterface {

	private static DateTypeValue instance;
	private String id;

	private DateTypeValue() {
		this.id = "date";
	}

	public static DateTypeValue getInstance() {
		if (DateTypeValue.instance == null) {
			DateTypeValue.instance = new DateTypeValue();
		}
		return DateTypeValue.instance;
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
		return "date";
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
		Date date= new Date();
		StringValue value = new StringValue(strValue);

		return date.apply(value);
	}

}
