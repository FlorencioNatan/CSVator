package org.csvator.interpreter.parsingTable.function.builtIn;

import java.util.LinkedList;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.CollectionValueInterface;
import org.csvator.interpreter.parsingTable.ValueInterface;
import org.csvator.interpreter.parsingTable.function.FunctionValueInterface;
import org.csvator.interpreter.parsingTable.function.InvalidNumberOfParametersException;
import org.csvator.interpreter.parsingTable.function.TypeMismatchException;
import org.csvator.interpreter.parsingTable.typeValues.AnyFunctionTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.AnyTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.CollectionTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.FunctionTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.TypeValueInterface;

public class Reduce implements FunctionValueInterface {

	@Override
	public String getId() {
		return "reduce";
	}

	@Override
	public ValueInterface evaluate(Environment env) {
		return this;
	}

	@Override
	public TypeValueInterface getType() {
		LinkedList<TypeValueInterface> parametersType = new LinkedList<>();
		parametersType.add(CollectionTypeValue.getInstance());
		parametersType.add(AnyFunctionTypeValue.getInstance());
		parametersType.add(AnyTypeValue.getInstance());
		return new FunctionTypeValue(parametersType, getReturnType());
	}

	public Environment createLocalEnvironment(LinkedList<ValueInterface> values, Environment father) throws TypeMismatchException {
		if (values.size() != 3) {
			throw new InvalidNumberOfParametersException("The function filter expects 3 parameters, but " + values.size() +  " are found.");
		}
		Environment local = new Environment();
		local.setFatherEnvironment(father);
		CollectionValueInterface collection = (CollectionValueInterface) values.get(0).evaluate(father);
		FunctionValueInterface reduceFunction = (FunctionValueInterface) values.get(1).evaluate(father);
		ValueInterface reduceValue = (ValueInterface) values.get(2).evaluate(father);

		local.putValue("collection", collection);
		local.putValue("reduceFunction", reduceFunction);
		local.putValue("reduceValue", reduceValue);
		return local;
	}

	@Override
	public TypeValueInterface getReturnType() {
		return AnyTypeValue.getInstance();
	}

	@Override
	public ValueInterface apply(Environment env) {
		CollectionValueInterface collection = (CollectionValueInterface) env.getValueOf("collection");
		FunctionValueInterface reduceFunction = (FunctionValueInterface) env.getValueOf("reduceFunction");
		ValueInterface reduceValue = env.getValueOf("reduceValue");

		return collection.reduce(reduceFunction, reduceValue);
	}

}
