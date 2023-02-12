package org.csvator.interpreter.environment.operators.comparsion;

import java.time.format.DateTimeFormatter;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.environment.operators.OperatorInterface;
import org.csvator.interpreter.parsingTable.BooleanValue;
import org.csvator.interpreter.parsingTable.DateTimeValue;
import org.csvator.interpreter.parsingTable.DateValue;
import org.csvator.interpreter.parsingTable.DoubleValue;
import org.csvator.interpreter.parsingTable.IntegerValue;
import org.csvator.interpreter.parsingTable.StringValue;
import org.csvator.interpreter.parsingTable.ValueInterface;
import org.csvator.interpreter.parsingTable.typeValues.DateTimeTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.DateTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.DoubleTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.IntTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.StringTypeValue;

public abstract class ComparsionOperator implements OperatorInterface {

	private BooleanValue comparsionResult;

	public ValueInterface apply(ValueInterface lho, ValueInterface rho, Environment env) {
		intComparsion(lho, rho, env);
		stringComparsion(lho, rho, env);
		defaultNumericComparsion(lho, rho, env);
		datetimeComparsion(lho, rho, env);
		defaultDateComparsion(lho, rho, env);

		return comparsionResult;
	}

	private void datetimeComparsion(ValueInterface lho, ValueInterface rho, Environment env) {
		if (lho.getType() == rho.getType() && lho.getType() == DateTimeTypeValue.getInstance()) {
			DateTimeValue datetimeLho = this.castToDateTimeValue(lho);
			DateTimeValue datetimeRho = this.castToDateTimeValue(rho);
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHms");
			String strLho = datetimeLho.getDateTimeValue().format(formatter);
			String strRho = datetimeRho.getDateTimeValue().format(formatter);

			boolean result = this.operationOnString(strLho, strRho);

			comparsionResult = this.createResult(datetimeLho, datetimeRho, result, env);
		}
	}

	private void defaultDateComparsion(ValueInterface lho, ValueInterface rho, Environment env) {
		String strLho = "";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

		if (lho.getType() == DateTimeTypeValue.getInstance()) {
			strLho = this.castToDateTimeValue(lho).getDateTimeValue().format(formatter);
		} else if(lho.getType() == DateTypeValue.getInstance()) {
			strLho = this.castToDateValue(lho).getDateValue().format(formatter);
		} else {
			return;
		}

		String strRho = "";
		if (rho.getType() == DateTimeTypeValue.getInstance()) {
			strRho = this.castToDateTimeValue(rho).getDateTimeValue().format(formatter);
		} else if(rho.getType() == DateTypeValue.getInstance()) {
			strRho = this.castToDateValue(rho).getDateValue().format(formatter);
		} else {
			return;
		}
		boolean result = this.operationOnString(strLho, strRho);

		comparsionResult = this.createResult(lho, rho, result, env);
	}

	private void defaultNumericComparsion(ValueInterface lho, ValueInterface rho, Environment env) {
		double doubleLho = 0.0;
		if (lho.getType() == IntTypeValue.getInstance()) {
			doubleLho = this.castToIntegerValue(lho).getIntValue();
		} else if(lho.getType() == DoubleTypeValue.getInstance()) {
			doubleLho = this.castToDoubleValue(lho).getDoubleValue();
		} else {
			return;
		}

		double doubleRho = 0.0;
		if (rho.getType() == IntTypeValue.getInstance()) {
			doubleRho = this.castToIntegerValue(rho).getIntValue();
		} else if(rho.getType() == DoubleTypeValue.getInstance()) {
			doubleRho = this.castToDoubleValue(rho).getDoubleValue();
		} else {
			return;
		}
		boolean result = this.operationOnDouble(doubleLho, doubleRho);

		comparsionResult = this.createResult(lho, rho, result, env);
	}

	private void stringComparsion(ValueInterface lho, ValueInterface rho, Environment env) {
		if (lho.getType() == rho.getType() && lho.getType() == StringTypeValue.getInstance()) {
			StringValue strLho = this.castToStringValue(lho);
			StringValue strRho = this.castToStringValue(rho);
			boolean result = this.operationOnString(strLho.getStrValue(), strRho.getStrValue());

			comparsionResult = this.createResult(strLho, strRho, result, env);
		}
	}

	private void intComparsion(ValueInterface lho, ValueInterface rho, Environment env) {
		if (lho.getType() == rho.getType() && lho.getType() == IntTypeValue.getInstance()) {
			IntegerValue intLho = this.castToIntegerValue(lho);
			IntegerValue intRho = this.castToIntegerValue(rho);
			boolean result = this.operationOnInt(intLho.getIntValue(), intRho.getIntValue());

			comparsionResult = this.createResult(intLho, intRho, result, env);
		}
	}

	abstract protected boolean operationOnInt(int lho, int rho);
	abstract protected boolean operationOnDouble(double lho, double rho);
	abstract protected boolean operationOnString(String lho, String rho);

	private IntegerValue castToIntegerValue(ValueInterface value) {
		return (IntegerValue) value;
	}

	private DoubleValue castToDoubleValue(ValueInterface value) {
		return (DoubleValue) value;
	}

	private StringValue castToStringValue(ValueInterface value) {
		return (StringValue) value;
	}

	private DateValue castToDateValue(ValueInterface value) {
		return (DateValue) value;
	}

	private DateTimeValue castToDateTimeValue(ValueInterface value) {
		return (DateTimeValue) value;
	}

	private BooleanValue createResult(ValueInterface lho, ValueInterface rho, boolean result, Environment env) {
		return new BooleanValue(lho.evaluate(env) + " " + rho.evaluate(env), result);
	}

}
