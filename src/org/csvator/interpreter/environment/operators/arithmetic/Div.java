package org.csvator.interpreter.environment.operators.arithmetic;

public class Div extends IntegerOperator {

	@Override
	protected int operationOnInt(int lho, int rho) {
		return lho / rho;
	}

	@Override
	protected double operationOnDouble(double lho, double rho) {
		return lho / rho;
	}

}
