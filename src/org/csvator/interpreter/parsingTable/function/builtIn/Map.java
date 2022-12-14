package org.csvator.interpreter.parsingTable.function.builtIn;

import java.util.LinkedList;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.CollectionValueInterface;
import org.csvator.interpreter.parsingTable.ValueInterface;
import org.csvator.interpreter.parsingTable.function.FunctionValueInterface;
import org.csvator.interpreter.parsingTable.function.InvalidNumberOfParametersException;
import org.csvator.interpreter.parsingTable.function.TypeMismatchException;
import org.csvator.interpreter.parsingTable.typeValues.AnyFunctionTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.CollectionTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.FunctionTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.TypeValueInterface;

public class Map implements FunctionValueInterface {

	@Override
	public String getId() {
		return "map";
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
		return new FunctionTypeValue(parametersType, getReturnType());
	}

	public Environment createLocalEnvironment(LinkedList<ValueInterface> values, Environment father) throws TypeMismatchException {
		if (values.size() != 2) {
			throw new InvalidNumberOfParametersException("The function map expects 2 parameters, but " + values.size() +  " are found.");
		}
		Environment local = new Environment();
		local.setFatherEnvironment(father);
		CollectionValueInterface collection = (CollectionValueInterface) values.get(0).evaluate(father);
		FunctionValueInterface sortFunction = (FunctionValueInterface) values.get(1).evaluate(father);

		local.putValue("collection", collection);
		local.putValue("mapFunction", sortFunction);
		return local;
	}

	@Override
	public TypeValueInterface getReturnType() {
		return CollectionTypeValue.getInstance();
	}

	@Override
	public ValueInterface apply(Environment env) {
		CollectionValueInterface collection = (CollectionValueInterface) env.getValueOf("collection");
		FunctionValueInterface mapFunction = (FunctionValueInterface) env.getValueOf("mapFunction");

		return collection.map(mapFunction);
	}

}
