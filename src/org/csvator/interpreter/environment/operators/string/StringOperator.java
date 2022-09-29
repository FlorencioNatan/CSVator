package org.csvator.interpreter.environment.operators.string;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.environment.operators.OperatorInterface;
import org.csvator.interpreter.parsingTable.IntegerValue;
import org.csvator.interpreter.parsingTable.StringValue;
import org.csvator.interpreter.parsingTable.ValueInterface;

public abstract class StringOperator implements OperatorInterface {

	protected StringValue castToStringValue(ValueInterface value) {
		return (StringValue) value;
	}

	protected StringValue createResult(StringValue lho, StringValue rho, String result, Environment env) {
		return new StringValue(lho.evaluate(env) + " " + rho.evaluate(env), result);
	}

}
