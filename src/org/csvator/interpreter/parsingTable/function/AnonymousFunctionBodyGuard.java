package org.csvator.interpreter.parsingTable.function;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.BooleanValue;
import org.csvator.interpreter.parsingTable.NullValue;
import org.csvator.interpreter.parsingTable.ValueInterface;
import org.csvator.interpreter.parsingTable.typeValues.TypeValueInterface;

public class AnonymousFunctionBodyGuard implements ValueInterface {

	private String id;
	private ValueInterface condition;
	private ValueInterface result;

	public AnonymousFunctionBodyGuard(ValueInterface condition, ValueInterface result) {
		this.condition = condition;
		this.result = result;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public ValueInterface evaluate(Environment env) {
		ValueInterface conditionEvaluated = condition.evaluate(env);
		if (conditionEvaluated instanceof BooleanValue && ((BooleanValue) conditionEvaluated).getBooleanValue() == true ) {
			return result.evaluate(env);
		}
		return NullValue.getInstace();
	}

	@Override
	public TypeValueInterface getType() {
		return result.getType();
	}

	public Guard toGuard() {
		return new Guard(condition, result);
	}

}
