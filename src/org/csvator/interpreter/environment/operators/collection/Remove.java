package org.csvator.interpreter.environment.operators.collection;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.CollectionValueInterface;
import org.csvator.interpreter.parsingTable.ValueInterface;

public class Remove extends CollectionOperator {

	@Override
	public ValueInterface apply(ValueInterface lho, ValueInterface rho, Environment env) {
		if (lho instanceof CollectionValueInterface) {
			CollectionValueInterface colLho = this.castToCollection(lho);
			colLho.remove(rho.evaluate(env));
			return colLho;
		}

		CollectionValueInterface colRho = this.castToCollection(rho);
		colRho.remove(lho.evaluate(env));
		return colRho;
	}

}
