package org.csvator.interpreter.parsingTable.typeValues;

import java.util.LinkedList;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.EmptyValue;
import org.csvator.interpreter.parsingTable.ValueInterface;

public class FunctionTypeValue implements TypeValueInterface {

	String id;
	LinkedList<TypeValueInterface> parametersTypes;
	TypeValueInterface returnType;

	public FunctionTypeValue(LinkedList<TypeValueInterface> parametersTypes, TypeValueInterface returnType) {
		this.id = "function";
		this.parametersTypes = parametersTypes;
		this.returnType = returnType;
	}

	public FunctionTypeValue(TypeValueInterface returnType) {
		this.id = "function";
		this.parametersTypes = new LinkedList<TypeValueInterface>();
		this.returnType = returnType;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public ValueInterface evaluate(Environment env) {
		return new EmptyValue(this.id);
	}

	@Override
	public String toString() {
		StringBuffer typeString = new StringBuffer();
		typeString.append("(");
		for (TypeValueInterface parameter : parametersTypes) {
			typeString.append(parameter);
			typeString.append(", ");
		}
		if (typeString.length() > 1) {
			typeString.delete(typeString.length() - 2, typeString.length());
		}
		typeString.append(" -> ");
		typeString.append(returnType);
		typeString.append(")");
		return typeString.toString();
	}

	@Override
	public TypeValueInterface getType() {
		return this;
	}

	public boolean compareToFunctionType(FunctionTypeValue type) {
		if (this.parametersTypes.size() != type.parametersTypes.size()) {
			return false;
		}

		if (this.returnType.getType() != type.returnType.getType()) {
			return false;
		}

		for (int i = 0; i < this.parametersTypes.size(); i++) {
			if (this.parametersTypes.get(i) != type.parametersTypes.get(i)) {
				return false;
			}
		}

		return true;
	}

	@Override
	public boolean equalsToType(TypeValueInterface type) {
		if (type instanceof AnyTypeValue) {
			return true;
		}

		if (type instanceof FunctionTypeValue) {
			return this.compareToFunctionType((FunctionTypeValue) type);
		}

		return false;
	}

}
