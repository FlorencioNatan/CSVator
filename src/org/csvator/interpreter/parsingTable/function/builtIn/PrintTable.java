package org.csvator.interpreter.parsingTable.function.builtIn;

import java.util.LinkedList;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.CollectionValueInterface;
import org.csvator.interpreter.parsingTable.NullValue;
import org.csvator.interpreter.parsingTable.ValueInterface;
import org.csvator.interpreter.parsingTable.function.FunctionValueInterface;
import org.csvator.interpreter.parsingTable.function.InvalidNumberOfParametersException;
import org.csvator.interpreter.parsingTable.function.TypeMismatchException;
import org.csvator.interpreter.parsingTable.typeValues.AnyFunctionTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.FunctionTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.TypeValueInterface;
import org.csvator.interpreter.parsingTable.typeValues.VoidTypeValue;
import org.csvator.interpreter.tablePrinterStrategy.SimpleCSVTablePrinter;

public class PrintTable implements FunctionValueInterface {

	@Override
	public String getId() {
		return "printTable";
	}

	@Override
	public ValueInterface evaluate(Environment env) {
		return this;
	}

	@Override
	public TypeValueInterface getType() {
		LinkedList<TypeValueInterface> parametersType = new LinkedList<>();
		parametersType.add(AnyFunctionTypeValue.getInstance());
		return new FunctionTypeValue(parametersType, getReturnType());
	}

	public Environment createLocalEnvironment(LinkedList<ValueInterface> values, Environment father) throws TypeMismatchException {
		if (values.size() != 1) {
			throw new InvalidNumberOfParametersException("The function printTable expects 1 parameter, but " + values.size() +  " are found.");
		}

		Environment local = new Environment();
		local.setFatherEnvironment(father);
		ValueInterface tableValue = (ValueInterface) values.get(0).evaluate(father);

		local.putValue("tableValue", tableValue);
		return local;
	}

	public TypeValueInterface getReturnType() {
		return VoidTypeValue.getInstance();
	}

	public ValueInterface apply(Environment env) {
		ValueInterface tableValue = (ValueInterface) env.getValueOf("tableValue");

		String[][] table = { { NullValue.getInstace().toString() } };
		if (tableValue instanceof CollectionValueInterface) {
			table = ((CollectionValueInterface) tableValue).buildTable();
		} else {
			table[0][0] = tableValue.toString();
		}

		SimpleCSVTablePrinter simple = new SimpleCSVTablePrinter();
		simple.printTable(table);

		return NullValue.getInstace();
	}

}
