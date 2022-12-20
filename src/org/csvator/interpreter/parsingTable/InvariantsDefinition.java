package org.csvator.interpreter.parsingTable;

import java.util.LinkedList;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.typeValues.TypeValueInterface;
import org.csvator.interpreter.parsingTable.typeValues.VoidTypeValue;

public class InvariantsDefinition implements ValueInterface {

	private LinkedList<ValueInterface> invariants;

	public InvariantsDefinition(LinkedList<ValueInterface> invariants) {
		this.invariants = invariants;
	}

	public LinkedList<ValueInterface> getInvariants() {
		return invariants;
	}

	@Override
	public String getId() {
		return "InvariantsDefinition";
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
		return "InvariantsDefinition";
	}

}
