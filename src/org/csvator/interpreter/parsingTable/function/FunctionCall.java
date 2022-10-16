package org.csvator.interpreter.parsingTable.function;

import java.util.LinkedList;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.ValueInterface;
import org.csvator.interpreter.parsingTable.typeValues.FunctionTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.TypeValueInterface;

public class FunctionCall implements ValueInterface {

	String id;

	public FunctionCall(String id) {
		this.id = id;
	}

	public Environment createLocalEnvironment(LinkedList<ValueInterface> values, Environment father) throws TypeMismatchException {
		FunctionValue value = (FunctionValue) father.getValueOf(id);
		return value.createLocalEnvironment(values, father);
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public ValueInterface evaluate(Environment env) {
		FunctionValue value = (FunctionValue) env.getValueOf(id);
		return value.evaluate(env);
	}

	public TypeValueInterface getReturnType(Environment env) {
		FunctionValue value = (FunctionValue) env.getValueOf(id);
		return value.getReturnType();
	}

	@Override
	public TypeValueInterface getType() {
		return new FunctionTypeValue(null);
	}

}
