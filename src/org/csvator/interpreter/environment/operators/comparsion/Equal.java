package org.csvator.interpreter.environment.operators.comparsion;

import org.csvator.interpreter.parsingTable.ValueInterface;

public class Equal extends EqualityOperator {

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

	@Override
	protected boolean operationOnBoolean(boolean lho, boolean rho) {
		return lho == rho;
	}

	@Override
	protected boolean operationOnNull(ValueInterface lho, ValueInterface rho) {
		return lho == rho;
	}

	@Override
	protected boolean operationOnDifferentClasses(ValueInterface lho, ValueInterface rho) {
		return false;
	}

}
