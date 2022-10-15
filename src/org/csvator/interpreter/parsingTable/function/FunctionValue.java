package org.csvator.interpreter.parsingTable.function;

import java.util.LinkedList;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.ArgumentValue;
import org.csvator.interpreter.parsingTable.DoubleValue;
import org.csvator.interpreter.parsingTable.EmptyValue;
import org.csvator.interpreter.parsingTable.ExpressionValueInterface;
import org.csvator.interpreter.parsingTable.IntegerValue;
import org.csvator.interpreter.parsingTable.ValueInterface;
import org.csvator.interpreter.parsingTable.typeValues.DoubleTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.IntTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.TypeValueInterface;

public class FunctionValue implements ValueInterface {

	String id;
	TypeValueInterface returnType;
	LinkedList<ArgumentValue> arguments;
	LinkedList<Guard> expressions;

	public FunctionValue(String id, TypeValueInterface returnType, LinkedList<ArgumentValue> arguments) {
		this.id = id;
		this.returnType = returnType;
		this.arguments = arguments;
		this.expressions = new LinkedList<>();
	}

	public FunctionValue(String id, TypeValueInterface returnType) {
		this.id = id;
		this.returnType = returnType;
		this.arguments = new LinkedList<>();
		this.expressions = new LinkedList<>();
	}

	public void addExpression(ExpressionValueInterface condition, ExpressionValueInterface result) {
		Guard guarda = new Guard(condition, result);
		this.expressions.add(guarda);
	}

	public Environment createLocalEnvironment(LinkedList<ValueInterface> values, Environment father) throws TypeMismatchException {
		Environment local = new Environment();
		local.setFatherEnvironment(father);
		for (int i = 0; i < arguments.size(); i++) {
			ValueInterface parameterValue = values.get(i).evaluate(father);
			if (arguments.get(i).getTypeClass() == DoubleTypeValue.class && parameterValue.getTypeClass() == IntTypeValue.class) {
				int parameterContet = ((IntegerValue) parameterValue).getIntValue(father);
				parameterValue = new DoubleValue(parameterValue.getId(), parameterContet);
			}

			if (arguments.get(i).getTypeClass() != parameterValue.getTypeClass()) {
				throw new TypeMismatchException("Type mismatch on function " + this.id.trim() + " parameter " + (i + 1) + ". Expected " + arguments.get(i).getTypeClass() + " found " + parameterValue.getTypeClass());
			}
			local.putValue(arguments.get(i).getIdVariable(), parameterValue);
		}

		return local;
	}

	@Override
	public String getId() {
		return this.id;
	}

	@SuppressWarnings("rawtypes")
	public Class getReturnType() {
		return this.returnType.getTypeClass();
	}

	@Override
	public ValueInterface evaluate(Environment env) {
		for (Guard guard : expressions) {
			if (guard.isConditionValid(env) ) {
				return guard.evaluate(env);
			}
		}
		return new EmptyValue("Empty");
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Class getTypeClass() {
		return this.getClass();
	}

}
