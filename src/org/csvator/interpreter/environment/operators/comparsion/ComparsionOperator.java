package org.csvator.interpreter.environment.operators.comparsion;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.environment.operators.OperatorInterface;
import org.csvator.interpreter.parsingTable.BooleanValue;
import org.csvator.interpreter.parsingTable.IntegerValue;
import org.csvator.interpreter.parsingTable.ValueInterface;

public abstract class ComparsionOperator implements OperatorInterface {

	protected IntegerValue castToIntegerValue(ValueInterface value) {
		return (IntegerValue) value;
	}

	protected BooleanValue createResult(IntegerValue lho, IntegerValue rho, boolean result, Environment env) {
		return new BooleanValue(lho.evaluate(env) + " " + rho.evaluate(env), result);
	}

}
