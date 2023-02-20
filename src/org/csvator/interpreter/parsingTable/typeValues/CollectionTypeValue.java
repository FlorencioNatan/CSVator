package org.csvator.interpreter.parsingTable.typeValues;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.ValueInterface;

public class CollectionTypeValue implements TypeValueInterface {

	private static CollectionTypeValue instance;
	private String id;

	public CollectionTypeValue() {
		this.id = "collection";
	}

	public static CollectionTypeValue getInstance() {
		if (CollectionTypeValue.instance == null) {
			CollectionTypeValue.instance = new CollectionTypeValue();
		}
		return CollectionTypeValue.instance;
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
		return "collection";
	}

	@Override
	public TypeValueInterface getType() {
		return this;
	}

	public boolean equalsToType(TypeValueInterface type) {
		if (type == this) {
			return true;
		}

		if (type instanceof AnyTypeValue) {
			return true;
		}

		if (type instanceof VectorTypeValue) {
			return true;
		}

		if (type instanceof DictTypeValue) {
			return true;
		}

		if (type instanceof ListTypeValue) {
			return true;
		}

		if (type instanceof SetTypeValue) {
			return true;
		}

		if (type instanceof StringTypeValue) {
			return true;
		}

		if (type instanceof RecordTypeValue) {
			return true;
		}

		return false;
	}

	@Override
	public ValueInterface createValue(String strValue) {
		return null;
	}

}
