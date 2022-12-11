package org.csvator.interpreter.parsingTable.function;

import java.util.LinkedList;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.ValueInterface;
import org.csvator.interpreter.parsingTable.typeValues.TypeValueInterface;

public interface FunctionValueInterface extends ValueInterface {

	public Environment createLocalEnvironment(LinkedList<ValueInterface> values, Environment father);

	public TypeValueInterface getReturnType();

	public ValueInterface apply(Environment env);

}
