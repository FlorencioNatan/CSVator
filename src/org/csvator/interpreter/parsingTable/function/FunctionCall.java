package org.csvator.interpreter.parsingTable.function;

import java.util.LinkedList;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.FunctionCallExpressionValue;
import org.csvator.interpreter.parsingTable.ValueInterface;
import org.csvator.interpreter.parsingTable.typeValues.FunctionTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.TypeValueInterface;

public class FunctionCall implements ValueInterface {

	String id;

	public FunctionCall(String id) {
		this.id = id;
	}

	public Environment createLocalEnvironment(LinkedList<ValueInterface> values, Environment father) throws TypeMismatchException {
		ValueInterface value = father.getValueOf(id);
		FunctionValueInterface functionValue;
		if (value instanceof FunctionCallExpressionValue) {
			functionValue = (FunctionValueInterface) value.evaluate(father);
		} else {
			functionValue = (FunctionValueInterface) value;
		}
		return functionValue.createLocalEnvironment(values, father);
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public ValueInterface evaluate(Environment env) {
		ValueInterface value = env.getValueOf(id);
		FunctionValueInterface functionValue;
		if (value instanceof FunctionCallExpressionValue) {
			functionValue = (FunctionValueInterface) value.evaluate(env);
		} else {
			functionValue = (FunctionValueInterface) value;
		}
		return functionValue.apply(env);
	}

	public TypeValueInterface getReturnType(Environment env) {
		ValueInterface value = env.getValueOf(id);
		FunctionValueInterface functionValue;
		if (value instanceof FunctionCallExpressionValue) {
			functionValue = (FunctionValueInterface) value.evaluate(env);
		} else {
			functionValue = (FunctionValueInterface) value;
		}
		return functionValue.getReturnType();
	}

	@Override
	public TypeValueInterface getType() {
		return new FunctionTypeValue(null);
	}

}
