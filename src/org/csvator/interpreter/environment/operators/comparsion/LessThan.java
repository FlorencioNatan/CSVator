package org.csvator.interpreter.environment.operators.comparsion;

public class LessThan extends ComparsionOperator {


	protected boolean operationOnInt(int lho, int rho) {
		return lho < rho;
	}

	protected boolean operationOnDouble(double lho, double rho) {
		return lho < rho;
	}

}
