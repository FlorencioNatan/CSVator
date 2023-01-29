package org.csvator.interpreter.parsingTable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.typeValues.DateTimeTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.TypeValueInterface;

public class DateTimeValue implements ValueInterface {

	private String id;
	private LocalDateTime value;

	public DateTimeValue(String id, LocalDateTime value) {
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

	public LocalDateTime getDateTimeValue() {
		return this.value;
	}

	@Override
	public String toString() {
		return this.value.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
	}

	@Override
	public TypeValueInterface getType() {
		return DateTimeTypeValue.getInstance();
	}

	@Override
	public int hashCode() {
		return this.value.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if ((obj instanceof DateTimeValue)) {
			return this.value == ((DateTimeValue) obj).value;
		}
		return false;
	}

}
