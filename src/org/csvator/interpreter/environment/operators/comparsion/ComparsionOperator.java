package org.csvator.interpreter.environment.operators.comparsion;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.environment.operators.OperatorInterface;
import org.csvator.interpreter.parsingTable.BooleanValue;
import org.csvator.interpreter.parsingTable.DoubleValue;
import org.csvator.interpreter.parsingTable.IntegerValue;
import org.csvator.interpreter.parsingTable.StringValue;
import org.csvator.interpreter.parsingTable.ValueInterface;
import org.csvator.interpreter.parsingTable.typeValues.IntTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.StringTypeValue;

public abstract class ComparsionOperator implements OperatorInterface {

	public ValueInterface apply(ValueInterface lho, ValueInterface rho, Environment env) {
		if (lho.getType() == rho.getType() && lho.getType() == IntTypeValue.getInstace()) {
			IntegerValue intLho = this.castToIntegerValue(lho);
			IntegerValue intRho = this.castToIntegerValue(rho);
			boolean result = this.operationOnInt(intLho.getIntValue(env), intRho.getIntValue(env));

			return this.createResult(intLho, intRho, result, env);
		}

		if (lho.getType() == rho.getType() && lho.getType() == StringTypeValue.getInstace()) {
			StringValue strLho = this.castToStringValue(lho);
			StringValue strRho = this.castToStringValue(rho);
			boolean result = this.operationOnString(strLho.getStrValue(env), strRho.getStrValue(env));

			return this.createResult(strLho, strRho, result, env);
		}

		double doubleLho = 0.0;
		if (lho.getType() == IntTypeValue.getInstace()) {
			doubleLho = this.castToIntegerValue(lho).getIntValue(env);
		} else {
			doubleLho = this.castToDoubleValue(lho).getDoubleValue(env);
		}

		double doubleRho = 0.0;
		if (rho.getType() == IntTypeValue.getInstace()) {
			doubleRho = this.castToIntegerValue(rho).getIntValue(env);
		} else {
			doubleRho = this.castToDoubleValue(rho).getDoubleValue(env);
		}
		boolean result = this.operationOnDouble(doubleLho, doubleRho);

		return this.createResult(lho, rho, result, env);
	}

	abstract protected boolean operationOnInt(int lho, int rho);
	abstract protected boolean operationOnDouble(double lho, double rho);
	abstract protected boolean operationOnString(String lho, String rho);

	protected IntegerValue castToIntegerValue(ValueInterface value) {
		return (IntegerValue) value;
	}

	protected DoubleValue castToDoubleValue(ValueInterface value) {
		return (DoubleValue) value;
	}

	protected StringValue castToStringValue(ValueInterface value) {
		return (StringValue) value;
	}

	protected BooleanValue createResult(ValueInterface lho, ValueInterface rho, boolean result, Environment env) {
		return new BooleanValue(lho.evaluate(env) + " " + rho.evaluate(env), result);
	}

}
