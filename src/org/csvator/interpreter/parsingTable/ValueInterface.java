package org.csvator.interpreter.parsingTable;

import org.csvator.interpreter.environment.Environment;

public interface ValueInterface {

	public String getId();

	public ValueInterface evaluate(Environment env);

	@SuppressWarnings("rawtypes")
	public Class getTypeClass();

}
