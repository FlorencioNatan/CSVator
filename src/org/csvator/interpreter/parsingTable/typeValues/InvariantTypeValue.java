package org.csvator.interpreter.parsingTable.typeValues;

import java.security.InvalidParameterException;
import java.util.LinkedList;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.BooleanValue;
import org.csvator.interpreter.parsingTable.DoubleValue;
import org.csvator.interpreter.parsingTable.IntegerValue;
import org.csvator.interpreter.parsingTable.ValueInterface;
import org.csvator.interpreter.parsingTable.function.FunctionValueInterface;
import org.csvator.interpreter.parsingTable.function.InvalidNumberOfParametersException;
import org.csvator.interpreter.parsingTable.function.TypeMismatchException;
import org.csvator.interpreter.parsingTable.function.UserDefinedFunctionValue;

public class InvariantTypeValue implements TypeValueInterface, FunctionValueInterface {

	private String id;
	private TypeValueInterface type;
	private LinkedList<ValueInterface> invariants;

	public InvariantTypeValue(TypeValueInterface type, LinkedList<ValueInterface> invariants) {
		this.type = type;
		this.invariants = invariants;
	}

	public void checkInvariants(ValueInterface value) {
		LinkedList<ValueInterface> valueList = new LinkedList<ValueInterface>();
		valueList.add(value);

		try {
			Environment env = this.createLocalEnvironment(valueList, null);
			for (ValueInterface invariant : invariants) {
				ValueInterface result = invariant.evaluate(env);
				if (!(result instanceof BooleanValue)) {
					throw new InvariantException("Invariant " + this.id + " violates invariant!");
				}

				BooleanValue boolResult = (BooleanValue) result;
				if (!boolResult.getBooleanValue()) {
					throw new InvariantException("Invariant " + this.id + " violates invariant!");
				}
			}
		} catch (NullPointerException e) {
			return;
		}
	}

	public LinkedList<ValueInterface> getInvariants() {
		return invariants;
	}

	@Override
	public String getId() {
		return "invariant " + this.id;
	}

	public String setId(String id) {
		return this.id = id;
	}

	@Override
	public ValueInterface evaluate(Environment env) {
		return this;
	}

	@Override
	public TypeValueInterface getType() {
		return this;
	}

	@Override
	public boolean equalsToType(TypeValueInterface type) {
		if (type instanceof AnyTypeValue) {
			return true;
		}

		if (this.type.equalsToType(type)) {
			return true;
		}

		return false;
	}

	@Override
	public Environment createLocalEnvironment(LinkedList<ValueInterface> values, Environment father) {
		if (values.size() > 1) {
			throw new InvalidNumberOfParametersException("The invariant " + this.id.trim() + " expects "
				+ " at most 1 parameter, but " + values.size() +  " are found.");
		}
		Environment local = new Environment();
		local.setFatherEnvironment(father);

		if (values.size() == 0) {
			return local;
		}

		ValueInterface parameterValue = values.get(0);
		if (type.getClass() != FunctionTypeValue.class) {
			parameterValue = parameterValue.evaluate(father);
		}

		if (type.getClass() == FunctionTypeValue.class) {
			try {
				parameterValue = parameterValue.evaluate(father);

				UserDefinedFunctionValue paramenterFunction = (UserDefinedFunctionValue) this.extractFunctionFromParameter(parameterValue, father);
				parameterValue = paramenterFunction;
			} catch (InvalidParameterException exception) {
				// do nothing
			}
		}

		if (type == DoubleTypeValue.getInstance() && parameterValue.getType() == IntTypeValue.getInstance()) {
			int parameterContent = ((IntegerValue) parameterValue).getIntValue();
			parameterValue = new DoubleValue(parameterValue.getId(), parameterContent);
		}

		if (!type.equalsToType(parameterValue.getType())) {
			throw new TypeMismatchException("Type mismatch on record " + this.id.trim() + " parameter " + 1 + ". Expected " + type + " found " + parameterValue.getType());
		}
		local.putValue("$", parameterValue);

		return local;
	}

	private UserDefinedFunctionValue extractFunctionFromParameter(ValueInterface parameterValue, Environment father) throws InvalidParameterException {
		if (parameterValue.getType().getClass() == FunctionTypeValue.class) {
			return (UserDefinedFunctionValue) parameterValue;
		}
		if (parameterValue.getType() == VariableTypeValue.getInstance()) {
			return (UserDefinedFunctionValue) father.getValueOf(parameterValue.getId());
		}
		throw new InvalidParameterException("The parameter is not a function");
	}

	@Override
	public TypeValueInterface getReturnType() {
		return this;
	}

	@Override
	public ValueInterface apply(Environment env) {
		ValueInterface value = env.getValueOf("$");
		this.checkInvariants(value);

		return value;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();

		str.append("invariants " + this.id + "\n");

		for (ValueInterface value : invariants) {
			str.append("\t\t" + value + "\n");
		}

		str.append("end\n");

		return str.toString();
	}

	@Override
	public ValueInterface createValue(String strValue) {
		return null;
	}

}
