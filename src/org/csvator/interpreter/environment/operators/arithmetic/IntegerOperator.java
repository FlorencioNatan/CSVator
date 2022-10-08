package org.csvator.interpreter.environment.operators.arithmetic;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.environment.operators.OperatorInterface;
import org.csvator.interpreter.parsingTable.DoubleValue;
import org.csvator.interpreter.parsingTable.IntegerValue;
import org.csvator.interpreter.parsingTable.ValueInterface;

public abstract class IntegerOperator implements OperatorInterface {

	public ValueInterface apply(ValueInterface lho, ValueInterface rho, Environment env) {
		if (lho.getTypeClass() == rho.getTypeClass() && lho.getTypeClass() == IntegerValue.class) {
			IntegerValue intLho = this.castToIntegerValue(lho);
			IntegerValue intRho = this.castToIntegerValue(rho);
			int result = this.operationOnInt(intLho.getIntValue(env), intRho.getIntValue(env));

			return this.createIntegerResult(intLho, intRho, result, env);
		}

		double doubleLho = 0.0;
		if (lho.getTypeClass() == IntegerValue.class) {
			doubleLho = this.castToIntegerValue(lho).getIntValue(env);
		} else {
			doubleLho = this.castToDoubleValue(lho).getDoubleValue(env);
		}

		double doubleRho = 0.0;
		if (rho.getTypeClass() == IntegerValue.class) {
			doubleRho = this.castToIntegerValue(rho).getIntValue(env);
		} else {
			doubleRho = this.castToDoubleValue(rho).getDoubleValue(env);
		}
		double result = this.operationOnDouble(doubleLho, doubleRho);

		return this.createDoubleResult(lho, rho, result, env);
	}

	abstract protected int operationOnInt(int lho, int rho);
	abstract protected double operationOnDouble(double lho, double rho);

	protected IntegerValue castToIntegerValue(ValueInterface value) {
		return (IntegerValue) value;
	}

	protected DoubleValue castToDoubleValue(ValueInterface value) {
		return (DoubleValue) value;
	}

	protected IntegerValue createIntegerResult(IntegerValue lho, IntegerValue rho, int result, Environment env) {
		return new IntegerValue(lho.evaluate(env) + " " + rho.evaluate(env), result);
	}

	protected DoubleValue createDoubleResult(ValueInterface lho, ValueInterface rho, double result, Environment env) {
		return new DoubleValue(lho.evaluate(env) + " " + rho.evaluate(env), result);
	}

}
