package org.csvator.interpreter.parsingTable;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.environment.operators.OperatorInterface;

public class BinaryExpressionValue implements ExpressionValueInterface {

	String id;
	OperatorInterface operator;
	ValueInterface lho;
	ValueInterface rho;

	public BinaryExpressionValue(String id, OperatorInterface operator, ValueInterface lho, ValueInterface rho) {
		this.id = id;
		this.operator = operator;
		this.lho = lho;
		this.rho = rho;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public ValueInterface evaluate(Environment env) {
		return operator.apply(lho.evaluate(env), rho.evaluate(env), env).evaluate(env);
	}

}
