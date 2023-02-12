package org.csvator.interpreter.environment.operators.comparsion;

public class Equal extends ComparsionOperator {

	@Override
	protected boolean operationOnInt(int lho, int rho) {
		return lho == rho;
	}

	@Override
	protected boolean operationOnDouble(double lho, double rho) {
		return lho == rho;
	}

	@Override
	protected boolean operationOnString(String lho, String rho) {
		return lho.equals(rho);
	}

}
