package org.csvator.interpreter.parsingTable.function;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.BooleanValue;
import org.csvator.interpreter.parsingTable.ExpressionValueInterface;
import org.csvator.interpreter.parsingTable.ValueInterface;

public class Guard {

	private ExpressionValueInterface condition;
	private ExpressionValueInterface result;

	public Guard(ExpressionValueInterface condition, ExpressionValueInterface result) {
		this.condition = condition;
		this.result = result;
	}

	public boolean isConditionValid(Environment env) {
		BooleanValue result = (BooleanValue) condition.evaluate(env);
		return result.getBooleanValue(env);
	}

	public ValueInterface evaluate(Environment env) {
		return this.result.evaluate(env);
	}

}
