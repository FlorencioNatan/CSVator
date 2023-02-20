package org.csvator.interpreter.environment.operators.comparsion;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.environment.operators.OperatorInterface;
import org.csvator.interpreter.parsingTable.BooleanValue;
import org.csvator.interpreter.parsingTable.ValueInterface;
import org.csvator.interpreter.parsingTable.typeValues.TypeValueInterface;

public class TypeIs implements OperatorInterface {

	@Override
	public ValueInterface apply(ValueInterface lho, ValueInterface rho, Environment env) {
		TypeValueInterface type = (TypeValueInterface) rho;
		return new BooleanValue(lho.toString() + rho.toString(), lho.getType().equals(type));
	}

}
