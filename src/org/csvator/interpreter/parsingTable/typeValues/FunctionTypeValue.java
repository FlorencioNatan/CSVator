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
		// TODO Auto-generated method stub
		return "bool";
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

}
