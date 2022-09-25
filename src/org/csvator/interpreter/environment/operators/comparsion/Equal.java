package org.csvator.interpreter.environment.operators.comparsion;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.BooleanValue;
import org.csvator.interpreter.parsingTable.IntegerValue;
import org.csvator.interpreter.parsingTable.ValueInterface;

public class Equal extends EqualityOperator {

	@Override
	public ValueInterface apply(ValueInterface lho, ValueInterface rho, Environment env) {
		boolean result = false;
		if (lho.getClass() != rho.getClass()) {
			return this.createResult(lho, rho, result, env);
		}

		if (lho instanceof IntegerValue) {
			result = ((IntegerValue) lho).getIntValue(env) == ((IntegerValue) rho).getIntValue(env);
		}

		if (lho instanceof BooleanValue) {
			result = ((BooleanValue) lho).getBooleanValue(env) == ((BooleanValue) rho).getBooleanValue(env);
		}

		return this.createResult(lho, rho, result, env);
	}

}
