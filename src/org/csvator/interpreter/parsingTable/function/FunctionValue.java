package org.csvator.interpreter.parsingTable.function;

import java.util.LinkedList;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.ArgumentValue;
import org.csvator.interpreter.parsingTable.DoubleValue;
import org.csvator.interpreter.parsingTable.EmptyValue;
import org.csvator.interpreter.parsingTable.ExpressionValueInterface;
import org.csvator.interpreter.parsingTable.FunctionExpressionValue;
import org.csvator.interpreter.parsingTable.IntegerValue;
import org.csvator.interpreter.parsingTable.ValueInterface;
import org.csvator.interpreter.parsingTable.typeValues.DoubleTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.FunctionTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.IntTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.TypeValueInterface;
import org.csvator.interpreter.parsingTable.typeValues.VariableTypeValue;

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
			ValueInterface parameterValue = values.get(i);
			if (arguments.get(i).getType().getClass() != FunctionTypeValue.class) {
				parameterValue = parameterValue.evaluate(father);
			}

			boolean functionsHaveSameType = false;
			if (arguments.get(i).getType().getClass() == FunctionTypeValue.class && parameterValue.getType() == VariableTypeValue.getInstace()) {
				ValueInterface parameterValueValue = father.getValueOf(parameterValue.getId());
				FunctionValue paramenterFunction;
				if (parameterValueValue instanceof FunctionExpressionValue) {
					paramenterFunction = (FunctionValue) father.getValueOf(parameterValueValue.getId());
				} else {
					paramenterFunction = (FunctionValue) parameterValueValue;
				}
				FunctionTypeValue paramenterType = (FunctionTypeValue) paramenterFunction.getType();
				FunctionTypeValue argumentType = (FunctionTypeValue) arguments.get(i).getType();
				functionsHaveSameType = paramenterType.compareToFunctionType(argumentType);
				parameterValue = paramenterFunction;
			}

			if (arguments.get(i).getType() == DoubleTypeValue.getInstace() && parameterValue.getType() == IntTypeValue.getInstace()) {
				int parameterContet = ((IntegerValue) parameterValue).getIntValue(father);
				parameterValue = new DoubleValue(parameterValue.getId(), parameterContet);
			}

			if (arguments.get(i).getType() != parameterValue.getType() && !functionsHaveSameType) {
				throw new TypeMismatchException("Type mismatch on function " + this.id.trim() + " parameter " + (i + 1) + ". Expected " + arguments.get(i).getType().getClass() + " found " + parameterValue.getType().getClass());
			}
			local.putValue(arguments.get(i).getIdVariable(), parameterValue);
		}

		return local;
	}

	@Override
	public String getId() {
		return this.id;
	}

	public TypeValueInterface getReturnType() {
		return this.returnType;
	}

	@Override
	public ValueInterface evaluate(Environment env) {
		return this;
	}

	public ValueInterface apply(Environment env) {
		for (Guard guard : expressions) {
			if (guard.isConditionValid(env) ) {
				return guard.evaluate(env);
			}
		}
		return new EmptyValue("Empty");
	}

	@Override
	public TypeValueInterface getType() {
		FunctionTypeValue type;
		if (arguments.size() == 0) {
			type = new FunctionTypeValue(returnType);
		} else {
			LinkedList<TypeValueInterface> parametersType = new LinkedList<>();
			for (ArgumentValue argument: arguments) {
				parametersType.add(argument.getType());
			}

			type = new FunctionTypeValue(parametersType, returnType);
		}
		return type;
	}

	@Override
	public String toString() {
		StringBuffer typeString = new StringBuffer();
		typeString.append("function " + id + ": ");
		for (ArgumentValue argument : arguments) {
			typeString.append(argument);
			typeString.append(", ");
		}
		typeString.delete(typeString.length() - 2, typeString.length());
		typeString.append(" -> ");
		typeString.append(returnType);
		return typeString.toString();
	}

}
