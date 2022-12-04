package org.csvator.interpreter.environment.operators.collection;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.CollectionValueInterface;
import org.csvator.interpreter.parsingTable.ValueInterface;

public class Concat extends CollectionOperator {

	@Override
	public ValueInterface apply(ValueInterface lho, ValueInterface rho, Environment env) {
		if (lho instanceof CollectionValueInterface) {
			CollectionValueInterface colLho = this.castToCollection(lho);
			return colLho.concatAtTail(rho.evaluate(env));
		}
		CollectionValueInterface colRho = this.castToCollection(rho);
		return colRho.concatAtHead(lho.evaluate(env));
	}

}
