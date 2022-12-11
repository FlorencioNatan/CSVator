package org.csvator.interpreter.parsingTable;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.typeValues.DoubleTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.TypeValueInterface;

public class DoubleValue implements ValueInterface {

	String id;
	double value;

	public DoubleValue(String id, String strValue) {
		this.id = id;
		this.value = Double.valueOf(strValue);
	}

	public DoubleValue(String id, double value) {
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

	public double getDoubleValue(Environment env) {
		return this.value;
	}

	@Override
	public String toString() {
		return Double.toString(value);
	}

	@Override
	public TypeValueInterface getType() {
		return DoubleTypeValue.getInstance();
	}

	@Override
	public int hashCode() {
		return Double.hashCode(this.value);
	}

	@Override
	public boolean equals(Object obj) {
		if ((obj instanceof IntegerValue)) {
			return this.value == ((IntegerValue) obj).value;
		}
		if ((obj instanceof DoubleValue)) {
			return this.value == ((DoubleValue) obj).value;
		}
		return false;
	}


}
