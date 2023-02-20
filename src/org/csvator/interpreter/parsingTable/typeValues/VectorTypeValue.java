package org.csvator.interpreter.parsingTable.typeValues;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.ValueInterface;
import org.csvator.interpreter.parsingTable.VectorValue;

public class VectorTypeValue implements TypeValueInterface {

	private static VectorTypeValue instance;
	private String id;

	public VectorTypeValue() {
		this.id = "vector";
	}

	public static VectorTypeValue getInstance() {
		if (VectorTypeValue.instance == null) {
			VectorTypeValue.instance = new VectorTypeValue();
		}
		return VectorTypeValue.instance;
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
		return "vector";
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

		if (type instanceof CollectionTypeValue) {
			return true;
		}

		return false;
	}

	@Override
	public ValueInterface createValue(String strValue) {
		return new VectorValue(strValue);
	}

}
