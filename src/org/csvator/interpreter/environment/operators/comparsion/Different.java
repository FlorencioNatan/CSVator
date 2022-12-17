package org.csvator.interpreter.environment.operators.comparsion;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.BooleanValue;
import org.csvator.interpreter.parsingTable.DoubleValue;
import org.csvator.interpreter.parsingTable.IntegerValue;
import org.csvator.interpreter.parsingTable.StringValue;
import org.csvator.interpreter.parsingTable.ValueInterface;

public class Different extends EqualityOperator {

	@Override
	public ValueInterface apply(ValueInterface lho, ValueInterface rho, Environment env) {
		boolean result = false;

		if (lho instanceof IntegerValue && rho instanceof DoubleValue) {
			result = ((IntegerValue) lho).getIntValue() != ((DoubleValue) rho).getDoubleValue();
		}

		if (lho instanceof DoubleValue && rho instanceof IntegerValue) {
			result = ((DoubleValue) lho).getDoubleValue() != ((IntegerValue) rho).getIntValue();
		}

		if (lho.getClass() != rho.getClass()) {
			return this.createResult(lho, rho, result, env);
		}

		if (lho instanceof IntegerValue) {
			result = ((IntegerValue) lho).getIntValue() != ((IntegerValue) rho).getIntValue();
		}

		if (lho instanceof DoubleValue) {
			result = ((DoubleValue) lho).getDoubleValue() != ((DoubleValue) rho).getDoubleValue();
		}

		if (lho instanceof StringValue) {
			result = !((StringValue) lho).getStrValue().equals(((StringValue) rho).getStrValue());
		}

		if (lho instanceof BooleanValue) {
			result = ((BooleanValue) lho).getBooleanValue() != ((BooleanValue) rho).getBooleanValue();
		}

		return this.createResult(lho, rho, result, env);
	}

}
