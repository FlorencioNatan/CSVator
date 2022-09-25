package org.csvator.interpreter.environment.operators;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.ValueInterface;

public interface OperatorInterface {

	public ValueInterface apply(ValueInterface lho, ValueInterface rho, Environment env);

}
