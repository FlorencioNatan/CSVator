package org.csvator.interpreter.parsingTable;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.environment.operators.OperatorInterface;
import org.csvator.interpreter.parsingTable.typeValues.DoubleTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.TypeValueInterface;

public class BinaryExpressionValue implements ExpressionValueInterface {

	private String id;
	private OperatorInterface operator;
	private ValueInterface lho;
	private ValueInterface rho;

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

	@Override
	public TypeValueInterface getType() {
		if (lho.getType() == rho.getType()) {
			return lho.getType();
		}

		if (lho.getType() == DoubleTypeValue.getInstance() || rho.getType() == DoubleTypeValue.getInstance()) {
			return DoubleTypeValue.getInstance();
		}

		return lho.getType();
	}

}
