package org.csvator.interpreter.parsingTable;

import java.util.LinkedList;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.typeValues.TypeValueInterface;
import org.csvator.interpreter.parsingTable.typeValues.VoidTypeValue;

public class FieldsDefinition implements ValueInterface {

	private LinkedList<FieldValue> fields;

	public FieldsDefinition(LinkedList<FieldValue> fields) {
		this.fields = fields;
	}

	public LinkedList<FieldValue> getFields() {
		return fields;
	}

	@Override
	public String getId() {
		return "FieldsDefinition";
	}

	@Override
	public ValueInterface evaluate(Environment env) {
		return NullValue.getInstace();
	}

	@Override
	public TypeValueInterface getType() {
		return VoidTypeValue.getInstance();
	}

	@Override
	public String toString() {
		return "FieldsDefinition";
	}

}
