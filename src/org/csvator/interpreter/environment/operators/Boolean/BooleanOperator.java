package org.csvator.interpreter.environment.operators.Boolean;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.environment.operators.OperatorInterface;
import org.csvator.interpreter.parsingTable.BooleanValue;
import org.csvator.interpreter.parsingTable.ValueInterface;

public abstract class BooleanOperator implements OperatorInterface {

	protected BooleanValue castToBooleanValue(ValueInterface value) {
		return (BooleanValue) value;
	}

	protected BooleanValue createResult(BooleanValue lho, BooleanValue rho, boolean result, Environment env) {
		String id = lho.evaluate(env) + " ";
		if (rho != null) {
			id = id + rho.evaluate(env);
		}

		return new BooleanValue(id, result);
	}

}
