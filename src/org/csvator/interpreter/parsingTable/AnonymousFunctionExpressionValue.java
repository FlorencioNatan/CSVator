package org.csvator.interpreter.parsingTable;


import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.function.UserDefinedFunctionValue;
import org.csvator.interpreter.parsingTable.typeValues.TypeValueInterface;

public class AnonymousFunctionExpressionValue implements ExpressionValueInterface {

	private String id;
	private UserDefinedFunctionValue function;

	public AnonymousFunctionExpressionValue(String id, UserDefinedFunctionValue function) {
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
