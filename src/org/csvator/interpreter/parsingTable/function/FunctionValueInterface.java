package org.csvator.interpreter.parsingTable.function;

import java.util.LinkedList;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.ValueInterface;
import org.csvator.interpreter.parsingTable.typeValues.TypeValueInterface;

public interface FunctionValueInterface extends ValueInterface {

	public Environment createLocalEnvironment(LinkedList<ValueInterface> values, Environment father);

	public TypeValueInterface getReturnType();

	public ValueInterface apply(Environment env);

	default ValueInterface apply(ValueInterface... arguments) {
		LinkedList<ValueInterface> values = new LinkedList<ValueInterface>();
		for (ValueInterface argument : arguments) {
			values.add(argument);
		}

		Environment local = this.createLocalEnvironment(values, null);
		return this.apply(local);
	}

}
