package org.csvator.interpreter.parsingTable;

import java.util.LinkedList;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.function.FunctionValue;
import org.csvator.interpreter.parsingTable.function.TypeMismatchException;

public class FunctionExpressionValue implements ExpressionValueInterface {

	String id;
	FunctionValue function;
	LinkedList<ValueInterface> expressions;

	public FunctionExpressionValue(String id, FunctionValue function, LinkedList<ValueInterface> expressions) {
		this.id = id;
		this.function = function;
		this.expressions = expressions;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public ValueInterface evaluate(Environment env) {
		Environment local = function.createLocalEnvironment(expressions, env);
		ValueInterface result = function.evaluate(local);
		if (result.getTypeClass() != function.getReturnType()) {
			throw new TypeMismatchException("Type mismatch on function " + this.id.trim() + " return. Expected " + function.getReturnType() + " found " + result.getTypeClass());
		}
		return function.evaluate(local);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class getTypeClass() {
		return FunctionValue.class;
	}

}
