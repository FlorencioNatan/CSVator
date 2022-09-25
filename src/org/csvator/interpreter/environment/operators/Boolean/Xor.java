package org.csvator.interpreter.environment.operators.Boolean;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.BooleanValue;
import org.csvator.interpreter.parsingTable.ValueInterface;

public class Xor extends BooleanOperator {

	@Override
	public ValueInterface apply(ValueInterface lho, ValueInterface rho, Environment env) {
		BooleanValue boolLho = this.castToBooleanValue(lho);
		BooleanValue boolRho = this.castToBooleanValue(rho);
		boolean result = boolLho.getBooleanValue(env) != boolRho.getBooleanValue(env);

		return this.createResult(boolLho, boolRho, result, env);
	}

}
