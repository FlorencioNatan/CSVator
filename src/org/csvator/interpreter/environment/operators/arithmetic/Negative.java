package org.csvator.interpreter.environment.operators.arithmetic;

public class Negative extends ArithmeticOperator {

	@Override
	protected int operationOnInt(int lho, int rho) {
		return -lho;
	}

	@Override
	protected double operationOnDouble(double lho, double rho) {
		return -lho;
	}

}
