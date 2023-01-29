package org.csvator.interpreter.parsingTable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.typeValues.DateTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.TypeValueInterface;

public class DateValue implements ValueInterface {

	private String id;
	private LocalDate value;

	public DateValue(String id, LocalDate value) {
		this.id = id;
		this.value = value;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public ValueInterface evaluate(Environment env) {
		return this;
	}

	public LocalDate getDateValue() {
		return this.value;
	}

	@Override
	public String toString() {
		return this.value.format(DateTimeFormatter.ISO_LOCAL_DATE);
	}

	@Override
	public TypeValueInterface getType() {
		return DateTypeValue.getInstance();
	}

	@Override
	public int hashCode() {
		return this.value.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if ((obj instanceof DateValue)) {
			return this.value == ((DateValue) obj).value;
		}
		return false;
	}

}
