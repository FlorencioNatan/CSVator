package org.csvator.interpreter.environment.operators.collection;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.CollectionValueInterface;
import org.csvator.interpreter.parsingTable.KeyValueExpressionValue;
import org.csvator.interpreter.parsingTable.ValueInterface;
import org.csvator.interpreter.parsingTable.function.TypeMismatchException;

public class Head extends CollectionOperator {

	@Override
	public ValueInterface apply(ValueInterface lho, ValueInterface rho, Environment env) {
		if (lho instanceof CollectionValueInterface) {
			CollectionValueInterface colLho = this.castToCollection(lho);
			return colLho.head();
		}

		if (lho instanceof KeyValueExpressionValue) {
			KeyValueExpressionValue keyValue = (KeyValueExpressionValue) lho;
			return keyValue.getKey();
		}

		throw new TypeMismatchException("It's not possible index the data type " + lho.getType());
	}

}
