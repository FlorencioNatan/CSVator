package org.csvator.interpreter.environment.operators.comparsion;

public class GreaterThan extends ComparsionOperator {

	protected boolean operationOnInt(int lho, int rho) {
		return lho > rho;
	}

	protected boolean operationOnDouble(double lho, double rho) {
		return lho > rho;
	}

}
