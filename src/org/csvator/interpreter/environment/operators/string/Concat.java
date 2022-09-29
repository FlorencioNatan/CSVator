package org.csvator.interpreter.environment.operators.string;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.StringValue;
import org.csvator.interpreter.parsingTable.ValueInterface;

public class Concat extends StringOperator {

	@Override
	public ValueInterface apply(ValueInterface lho, ValueInterface rho, Environment env) {
		StringValue strLho = this.castToStringValue(lho);
		StringValue strRho = this.castToStringValue(rho);
		String result = strLho.getStrValue(env) + strRho.getStrValue(env);

		return this.createResult(strLho, strRho, result, env);
	}

}
