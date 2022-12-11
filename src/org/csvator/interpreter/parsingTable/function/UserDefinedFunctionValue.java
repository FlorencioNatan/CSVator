package org.csvator.interpreter.parsingTable.function;

import java.security.InvalidParameterException;
import java.util.LinkedList;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.ArgumentValue;
import org.csvator.interpreter.parsingTable.DoubleValue;
import org.csvator.interpreter.parsingTable.EmptyValue;
import org.csvator.interpreter.parsingTable.ExpressionValueInterface;
import org.csvator.interpreter.parsingTable.FunctionCallExpressionValue;
import org.csvator.interpreter.parsingTable.IntegerValue;
import org.csvator.interpreter.parsingTable.ValueInterface;
import org.csvator.interpreter.parsingTable.typeValues.AnyTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.DoubleTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.FunctionTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.IntTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.TypeValueInterface;
import org.csvator.interpreter.parsingTable.typeValues.VariableTypeValue;

public class UserDefinedFunctionValue implements FunctionValueInterface, Cloneable {

	String id;
	TypeValueInterface returnType;
	LinkedList<ArgumentValue> arguments;
	LinkedList<Guard> expressions;
	Environment clousure;

	public UserDefinedFunctionValue(String id, TypeValueInterface returnType, LinkedList<ArgumentValue> arguments) {
		this.id = id;
		this.returnType = returnType;
		this.arguments = arguments;
		this.expressions = new LinkedList<>();
	}

	public UserDefinedFunctionValue(String id, TypeValueInterface returnType) {
		this.id = id;
		this.returnType = returnType;
		this.arguments = new LinkedList<>();
		this.expressions = new LinkedList<>();
	}

	public void addExpression(ExpressionValueInterface condition, ExpressionValueInterface result) {
		Guard guarda = new Guard(condition, result);
		this.expressions.add(guarda);
	}

	public void setExpressions(LinkedList<Guard> expressions) {
		this.expressions = expressions;
	}

	public void setClousure(Environment clousure) {
		this.clousure = clousure;
	}

	public Environment createLocalEnvironment(LinkedList<ValueInterface> values, Environment father) throws TypeMismatchException {
		Environment local = new Environment();
		if (clousure == null) {
			local.setFatherEnvironment(father);
		} else {
			clousure.setFatherEnvironment(father);
			local.setFatherEnvironment(clousure);
		}
		for (int i = 0; i < arguments.size(); i++) {
			ValueInterface parameterValue = values.get(i);
			if (arguments.get(i).getType().getClass() != FunctionTypeValue.class) {
				parameterValue = parameterValue.evaluate(father);
			}

			boolean functionsHaveSameType = false;
			if (arguments.get(i).getType().getClass() == FunctionTypeValue.class) {
				try {
					UserDefinedFunctionValue paramenterFunction = (UserDefinedFunctionValue) this.extractFunctionFromParameter(parameterValue, father);
					FunctionTypeValue paramenterType = (FunctionTypeValue) paramenterFunction.getType();
					FunctionTypeValue argumentType = (FunctionTypeValue) arguments.get(i).getType();
					functionsHaveSameType = paramenterType.compareToFunctionType(argumentType);
					parameterValue = paramenterFunction;
				} catch (InvalidParameterException exception) {
					functionsHaveSameType = false;
					if (parameterValue instanceof FunctionCallExpressionValue) {
						parameterValue = parameterValue.evaluate(father);
					}
				}
			}

			if (arguments.get(i).getType() == DoubleTypeValue.getInstace() && parameterValue.getType() == IntTypeValue.getInstace()) {
				int parameterContet = ((IntegerValue) parameterValue).getIntValue(father);
				parameterValue = new DoubleValue(parameterValue.getId(), parameterContet);
			}

			if (arguments.get(i).getType() != AnyTypeValue.getInstace() && arguments.get(i).getType() != parameterValue.getType() && !functionsHaveSameType) {
				throw new TypeMismatchException("Type mismatch on function " + this.id.trim() + " parameter " + (i + 1) + ". Expected " + arguments.get(i).getType() + " found " + parameterValue.getType());
			}
			local.putValue(arguments.get(i).getIdVariable(), parameterValue);
		}

		return local;
	}

	private UserDefinedFunctionValue extractFunctionFromParameter(ValueInterface parameterValue, Environment father) throws InvalidParameterException {
		if (parameterValue instanceof FunctionCallExpressionValue) {
			ValueInterface function = parameterValue.evaluate(father);
			if (!(function.getType() instanceof FunctionTypeValue)) {
				throw new InvalidParameterException("The parameter is not a function");
			}
			return (UserDefinedFunctionValue) function;
		}
		if (parameterValue.getType().getClass() == FunctionTypeValue.class) {
			return (UserDefinedFunctionValue) parameterValue;
		}
		if (parameterValue.getType() == VariableTypeValue.getInstace()) {
			return (UserDefinedFunctionValue) father.getValueOf(parameterValue.getId());
		}
		throw new InvalidParameterException("The parameter is not a function");
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
		if (typeString.length() > 1) {
			typeString.delete(typeString.length() - 2, typeString.length());
		}
		typeString.append(" -> ");
		typeString.append(returnType);
		return typeString.toString();
	}

	@Override
	public UserDefinedFunctionValue clone() {
		UserDefinedFunctionValue newFunction = new UserDefinedFunctionValue(this.id, this.returnType, this.arguments);
		newFunction.setExpressions(this.expressions);
		return newFunction;
	}

}
