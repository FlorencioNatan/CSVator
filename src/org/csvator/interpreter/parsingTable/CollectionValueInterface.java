package org.csvator.interpreter.parsingTable;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.function.FunctionValueInterface;

public interface CollectionValueInterface extends ValueInterface {

	public CollectionValueInterface concatAtHead(ValueInterface value);

	public CollectionValueInterface concatAtTail(ValueInterface value);

	public void remove(ValueInterface value);

	public ValueInterface get(ValueInterface value);

	public ValueInterface contains(ValueInterface value);

	public ValueInterface head();

	public ValueInterface tail();

	public ValueInterface size();

	public CollectionValueInterface update(ValueInterface index, ValueInterface value);

	public CollectionValueInterface swap(ValueInterface firstIndex, ValueInterface secondIndex);

	public CollectionValueInterface sort(FunctionValueInterface sortFunction);

	public CollectionValueInterface map(FunctionValueInterface mapFunction);

	public CollectionValueInterface filter(FunctionValueInterface filterFunction);

	public ValueInterface reduce(FunctionValueInterface reduceFunction, ValueInterface reduceValue);

	public boolean universal(String variable, ValueInterface filter, Environment env);

	public boolean existential(String variable, ValueInterface filter, Environment env);

	public String[][] buildTable();

	public String[] buildTableLine();

	public String[] buildTableHeader();

}
