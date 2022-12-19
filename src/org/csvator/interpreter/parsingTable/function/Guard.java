package org.csvator.interpreter.parsingTable.function;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.AnonymousFunctionExpressionValue;
import org.csvator.interpreter.parsingTable.BooleanValue;
import org.csvator.interpreter.parsingTable.ValueInterface;

public class Guard {

	private ValueInterface condition;
	private ValueInterface result;

	public Guard(ValueInterface condition, ValueInterface result) {
		this.condition = condition;
		this.result = result;
	}

	public boolean isConditionValid(Environment env) {
		BooleanValue result = (BooleanValue) condition.evaluate(env);
		return result.getBooleanValue();
	}

	public ValueInterface evaluate(Environment env) {
		if (this.result.getClass() == AnonymousFunctionExpressionValue.class) {
			UserDefinedFunctionValue anonymousFunction = (UserDefinedFunctionValue) this.result.evaluate(env);
			Environment closure = env.clone();
			closure.setFatherEnvironment(null);
			anonymousFunction.setClosure(closure);
			return anonymousFunction;
		}
		return this.result.evaluate(env);
	}

}
