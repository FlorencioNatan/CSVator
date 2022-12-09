package org.csvator.interpreter.environment.operators.collection;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.CollectionValueInterface;
import org.csvator.interpreter.parsingTable.ValueInterface;
import org.csvator.interpreter.parsingTable.function.TypeMismatchException;

public class Contains extends CollectionOperator {

	@Override
	public ValueInterface apply(ValueInterface lho, ValueInterface rho, Environment env) {
		if (lho instanceof CollectionValueInterface) {
			CollectionValueInterface colLho = this.castToCollection(lho);
			return colLho.contains(rho.evaluate(env));
		}

		throw new TypeMismatchException("Contains must be called in a collection.");
	}

}
