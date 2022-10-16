package org.csvator.interpreter.parsingTable;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.typeValues.TypeValueInterface;

public interface ValueInterface {

	public String getId();

	public ValueInterface evaluate(Environment env);

	public TypeValueInterface getType();

}
