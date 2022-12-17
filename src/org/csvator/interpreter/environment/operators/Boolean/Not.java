package org.csvator.interpreter.environment.operators.Boolean;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.BooleanValue;
import org.csvator.interpreter.parsingTable.ValueInterface;

public class Not extends BooleanOperator {

	@Override
	public ValueInterface apply(ValueInterface lho, ValueInterface rho, Environment env) {
		BooleanValue boolLho = this.castToBooleanValue(lho);
		boolean result = !boolLho.getBooleanValue();

		return this.createResult(boolLho, null, result, env);
	}

}
