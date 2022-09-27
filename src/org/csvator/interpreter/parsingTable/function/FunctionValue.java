package org.csvator.interpreter.parsingTable.function;

import java.util.LinkedList;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.ArgumentValue;
import org.csvator.interpreter.parsingTable.EmptyValue;
import org.csvator.interpreter.parsingTable.ExpressionValueInterface;
import org.csvator.interpreter.parsingTable.ValueInterface;

public class FunctionValue implements ValueInterface {

	String id;
	LinkedList<ArgumentValue> arguments;
	LinkedList<Guard> expressions;

	public FunctionValue(String id, LinkedList<ArgumentValue> arguments) {
		this.id = id;
		this.arguments = arguments;
		this.expressions = new LinkedList<>();
	}

	public FunctionValue(String id) {
		this.id = id;
		this.arguments = new LinkedList<>();
		this.expressions = new LinkedList<>();
	}

	public void addExpression(ExpressionValueInterface condition, ExpressionValueInterface result) {
		Guard guarda = new Guard(condition, result);
		this.expressions.add(guarda);
	}

	public Environment createLocalEnvironment(LinkedList<ValueInterface> values, Environment father) {
		Environment local = new Environment();
		local.setFatherEnvironment(father);
		for (int i = 0; i < arguments.size(); i++) {
			local.putValue(arguments.get(i).getIdVariable(), values.get(i).evaluate(father));
		}

		return local;
	}

	@Override
	public String getId() {
		return this.id;
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

}