package org.csvator.interpreter.parsingTable.typeValues;

import java.util.LinkedList;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.ValueInterface;

public class UnionTypeValue implements TypeValueInterface {

	private String id;
	private LinkedList<TypeValueInterface> types;

	public UnionTypeValue(LinkedList<TypeValueInterface> types) {
		this.types = types;
	}

	@Override
	public String getId() {
		return this.id;
	}

	public String setId(String id) {
		return this.id = id;
	}

	@Override
	public ValueInterface evaluate(Environment env) {
		return this;
	}

	@Override
	public TypeValueInterface getType() {
		return this;
	}

	@Override
	public boolean equalsToType(TypeValueInterface type) {
		if (type instanceof AnyTypeValue) {
			return true;
		}

		for(TypeValueInterface typeComp : types) {
			if (typeComp.equalsToType(type)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();

		str.append("union " + this.id + "\n");

		for (TypeValueInterface type : types) {
			str.append("\t\t" + type + "\n");
		}

		str.append("end\n");

		return str.toString();
	}

	@Override
	public ValueInterface createValue(String strValue) {
		return null;
	}

}
