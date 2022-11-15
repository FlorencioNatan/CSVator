package org.csvator.interpreter.parsingTable;


import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.function.FunctionValue;
import org.csvator.interpreter.parsingTable.typeValues.TypeValueInterface;

public class AnonymousFunctionExpressionValue implements ExpressionValueInterface {

	String id;
	FunctionValue function;

	public AnonymousFunctionExpressionValue(String id, FunctionValue function) {
		this.id = id;
		this.function = function;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public ValueInterface evaluate(Environment env) {
		return function.clone();
	}

	@Override
	public TypeValueInterface getType() {
		return function.getType();
	}

}
