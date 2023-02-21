package org.csvator.interpreter.parsingTable;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.function.FunctionValueInterface;
import org.csvator.interpreter.parsingTable.typeValues.InvariantTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.TypeValueInterface;

public class CollectionWithIvariantValue implements CollectionValueInterface {

	private CollectionValueInterface collection;
	private InvariantTypeValue invariantType;

	public CollectionWithIvariantValue(CollectionValueInterface collection, InvariantTypeValue invariantType) {
		this.collection = collection;
		this.invariantType = invariantType;
	}

	@Override
	public String getId() {
		return collection.getId();
	}

	@Override
	public ValueInterface evaluate(Environment env) {
		return this;
	}

	@Override
	public TypeValueInterface getType() {
		return collection.getType();
	}

	@Override
	public CollectionValueInterface concatAtHead(ValueInterface value) {
		collection.concatAtHead(value);
		invariantType.checkInvariants(collection);
		return this;
	}

	@Override
	public CollectionValueInterface concatAtTail(ValueInterface value) {
		collection.concatAtTail(value);
		invariantType.checkInvariants(collection);
		return this;
	}

	@Override
	public void remove(ValueInterface value) {
		collection.remove(value);
		invariantType.checkInvariants(collection);
	}

	@Override
	public ValueInterface get(ValueInterface value) {
		return collection.get(value);
	}

	@Override
	public ValueInterface contains(ValueInterface value) {
		return collection.contains(value);
	}

	@Override
	public ValueInterface head() {
		return collection.head();
	}

	@Override
	public ValueInterface tail() {
		return collection.tail();
	}

	@Override
	public ValueInterface size() {
		return collection.size();
	}

	@Override
	public CollectionValueInterface update(ValueInterface index, ValueInterface value) {
		collection.update(index, value);
		invariantType.checkInvariants(collection);
		return this;
	}

	@Override
	public CollectionValueInterface swap(ValueInterface firstIndex, ValueInterface secondIndex) {
		collection.swap(firstIndex, secondIndex);
		invariantType.checkInvariants(collection);
		return this;
	}

	@Override
	public CollectionValueInterface sort(FunctionValueInterface sortFunction) {
		collection.sort(sortFunction);
		invariantType.checkInvariants(collection);
		return this;
	}

	@Override
	public CollectionValueInterface map(FunctionValueInterface mapFunction) {
		return collection.map(mapFunction);
	}

	@Override
	public CollectionValueInterface filter(FunctionValueInterface filterFunction) {
		return collection.filter(filterFunction);
	}

	@Override
	public ValueInterface reduce(FunctionValueInterface reduceFunction, ValueInterface reduceValue) {
		return collection.reduce(reduceFunction, reduceValue);
	}

	@Override
	public boolean universal(String variable, ValueInterface filter, Environment env) {
		return collection.universal(variable, filter, env);
	}

	@Override
	public boolean existential(String variable, ValueInterface filter, Environment env) {
		return collection.existential(variable, filter, env);
	}

	@Override
	public String[][] buildTable() {
		return collection.buildTable();
	}

	@Override
	public String[] buildTableLine() {
		return collection.buildTableLine();
	}

	@Override
	public String[] buildTableHeader() {
		return collection.buildTableHeader();
	}

	@Override
	public String toString() {
		return collection.toString();
	}

}
