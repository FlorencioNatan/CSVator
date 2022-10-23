package org.csvator.interpreter.parsingTable;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.typeValues.TypeValueInterface;

public class ArgumentValue implements ValueInterface {

	String id;
	String idVariable;
	TypeValueInterface type;

	public ArgumentValue(String id, String idVariable, TypeValueInterface type) {
		this.id = id;
		this.idVariable = idVariable;
		this.type = type;
	}

	public String getIdVariable() {
		return idVariable;
	}

	public TypeValueInterface getArgumentType() {
		return type;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public ValueInterface evaluate(Environment env) {
		return env.getValueOf(idVariable).evaluate(env);
	}

	@Override
	public TypeValueInterface getType() {
		return this.type.getType();
	}

	@Override
	public String toString() {
		return type.toString() + " " + idVariable;
	}

}
