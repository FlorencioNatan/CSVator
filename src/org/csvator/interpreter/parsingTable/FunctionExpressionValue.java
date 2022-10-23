package org.csvator.interpreter.parsingTable;

import java.util.LinkedList;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.function.FunctionCall;
import org.csvator.interpreter.parsingTable.function.TypeMismatchException;
import org.csvator.interpreter.parsingTable.typeValues.FunctionTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.TypeValueInterface;

public class FunctionExpressionValue implements ExpressionValueInterface {

	String id;
	FunctionCall call;
	LinkedList<ValueInterface> expressions;

	public FunctionExpressionValue(String id, FunctionCall call, LinkedList<ValueInterface> expressions) {
		this.id = id;
		this.call = call;
		this.expressions = expressions;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public ValueInterface evaluate(Environment env) {
		Environment local = call.createLocalEnvironment(expressions, env);
		ValueInterface result = call.evaluate(local);

		boolean functionsHaveSameType = false;
		if (result.getType().getClass() == FunctionTypeValue.class && call.getReturnType(env).getClass() == FunctionTypeValue.class) {
			FunctionTypeValue resultType = (FunctionTypeValue) result.getType();
			FunctionTypeValue callType = (FunctionTypeValue) call.getReturnType(env);
			functionsHaveSameType = resultType.compareToFunctionType(callType);
		}

		if (result.getType() != call.getReturnType(env) && !functionsHaveSameType) {
			throw new TypeMismatchException("Type mismatch on function " + this.id.trim() + " return. Expected " + call.getReturnType(env) + " found " + result.getType());
		}
		return call.evaluate(local);
	}

	@Override
	public TypeValueInterface getType() {
		return new FunctionTypeValue(null);
	}

}
