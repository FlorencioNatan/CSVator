package org.csvator.interpreter.parsingTable.function;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.BooleanValue;
import org.csvator.interpreter.parsingTable.ExpressionValueInterface;
import org.csvator.interpreter.parsingTable.NullValue;
import org.csvator.interpreter.parsingTable.ValueInterface;
import org.csvator.interpreter.parsingTable.typeValues.TypeValueInterface;

public class AnonymousFunctionBodyGuard implements ValueInterface {

	private String id;
	private ExpressionValueInterface condition;
	private ExpressionValueInterface result;

	public AnonymousFunctionBodyGuard(ExpressionValueInterface condition, ExpressionValueInterface result) {
		this.condition = condition;
		this.result = result;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public ValueInterface evaluate(Environment env) {
		// TODO Auto-generated method stub
		ValueInterface conditionEvaluated = condition.evaluate(env);
		if (conditionEvaluated instanceof BooleanValue && ((BooleanValue) conditionEvaluated).getBooleanValue(env) == true ) {
			return result.evaluate(env);
		}
		return NullValue.getInstace();
	}

	@Override
	public TypeValueInterface getType() {
		// TODO Auto-generated method stub
		return result.getType();
	}

	public Guard toGuard() {
		return new Guard(condition, result);
	}

}
