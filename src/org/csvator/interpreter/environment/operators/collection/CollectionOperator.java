package org.csvator.interpreter.environment.operators.collection;

import org.csvator.interpreter.environment.operators.OperatorInterface;
import org.csvator.interpreter.parsingTable.CollectionValueInterface;
import org.csvator.interpreter.parsingTable.ValueInterface;

public abstract class CollectionOperator implements OperatorInterface {

	protected CollectionValueInterface castToCollection(ValueInterface value) {
		return (CollectionValueInterface) value;
	}

}
