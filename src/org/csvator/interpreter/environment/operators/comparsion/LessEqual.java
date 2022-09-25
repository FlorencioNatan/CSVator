package org.csvator.interpreter.environment.operators.comparsion;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.IntegerValue;
import org.csvator.interpreter.parsingTable.ValueInterface;

public class LessEqual extends ComparsionOperator {

	@Override
	public ValueInterface apply(ValueInterface lho, ValueInterface rho, Environment env) {
		IntegerValue intLho = this.castToIntegerValue(lho);
		IntegerValue intRho = this.castToIntegerValue(rho);
		boolean result = intLho.getIntValue(env) <= intRho.getIntValue(env);

		return this.createResult(intLho, intRho, result, env);
	}

}
