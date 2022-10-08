package org.csvator.interpreter.parsingTable;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.environment.operators.OperatorInterface;

public class UnaryExpressionValue implements ExpressionValueInterface {

	String id;
	OperatorInterface operator;
	ValueInterface lho;

	public UnaryExpressionValue(String id, OperatorInterface operator, ValueInterface lho) {
		this.id = id;
		this.operator = operator;
		this.lho = lho;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public ValueInterface evaluate(Environment env) {
		return operator.apply(lho.evaluate(env), null, env).evaluate(env);
	}

	@Override
	public Class getTypeClass() {
		return this.lho.getTypeClass();
	}

}
