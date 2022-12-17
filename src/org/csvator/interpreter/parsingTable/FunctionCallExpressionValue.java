package org.csvator.interpreter.parsingTable;

import java.util.LinkedList;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.function.FunctionCall;
import org.csvator.interpreter.parsingTable.function.TypeMismatchException;
import org.csvator.interpreter.parsingTable.typeValues.FunctionTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.TypeValueInterface;

public class FunctionCallExpressionValue implements ExpressionValueInterface {

	private String id;
	private FunctionCall call;
	private LinkedList<ValueInterface> expressions;

	public FunctionCallExpressionValue(String id, FunctionCall call, LinkedList<ValueInterface> expressions) {
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

		if (!call.getReturnType(env).equalsToType(result.getType())) {
			throw new TypeMismatchException("Type mismatch on function " + this.id.trim() + " return. Expected " + call.getReturnType(env) + " found " + result.getType());
		}
		return result;
	}

	@Override
	public TypeValueInterface getType() {
		return new FunctionTypeValue(null);
	}

}
