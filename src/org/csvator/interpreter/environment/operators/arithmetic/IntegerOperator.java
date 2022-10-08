package org.csvator.interpreter.environment.operators.integer;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.environment.operators.OperatorInterface;
import org.csvator.interpreter.parsingTable.IntegerValue;
import org.csvator.interpreter.parsingTable.ValueInterface;

public abstract class IntegerOperator implements OperatorInterface {

	protected IntegerValue castToIntegerValue(ValueInterface value) {
		return (IntegerValue) value;
	}

	protected IntegerValue createResult(IntegerValue lho, IntegerValue rho, int result, Environment env) {
		return new IntegerValue(lho.evaluate(env) + " " + rho.evaluate(env), result);
	}

}
