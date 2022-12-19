package org.csvator.interpreter.parsingTable;

import java.util.LinkedList;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.function.FunctionValueInterface;
import org.csvator.interpreter.parsingTable.function.TypeMismatchException;
import org.csvator.interpreter.parsingTable.typeValues.FunctionTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.TypeValueInterface;

public class FunctionCallExpressionValue implements ValueInterface {

	private String id;
	private LinkedList<ValueInterface> expressions;

	public FunctionCallExpressionValue(String id, LinkedList<ValueInterface> expressions) {
		this.id = id;
		this.expressions = expressions;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public ValueInterface evaluate(Environment env) {
		Environment local = this.createLocalEnvironment(expressions, env);
		ValueInterface result = this.apply(local);

		TypeValueInterface returnType = this.getReturnType(env);
		if (!returnType.equalsToType(result.getType())) {
			throw new TypeMismatchException("Type mismatch on function " + this.id.trim() + " return. Expected " + returnType + " found " + result.getType());
		}
		return result;
	}

	private Environment createLocalEnvironment(LinkedList<ValueInterface> values, Environment father) throws TypeMismatchException {
		ValueInterface value = father.getValueOf(id);
		FunctionValueInterface functionValue;
		functionValue = (FunctionValueInterface) value.evaluate(father);
		return functionValue.createLocalEnvironment(values, father);
	}

	private ValueInterface apply(Environment env) {
		ValueInterface value = env.getValueOf(id);
		FunctionValueInterface functionValue;
		functionValue = (FunctionValueInterface) value.evaluate(env);
		return functionValue.apply(env);
	}

	private TypeValueInterface getReturnType(Environment env) {
		ValueInterface value = env.getValueOf(id);
		FunctionValueInterface functionValue;
		functionValue = (FunctionValueInterface) value.evaluate(env);
		return functionValue.getReturnType();
	}

	@Override
	public TypeValueInterface getType() {
		return new FunctionTypeValue(null);
	}

}
