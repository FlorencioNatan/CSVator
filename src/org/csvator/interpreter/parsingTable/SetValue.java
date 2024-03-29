package org.csvator.interpreter.parsingTable;


import java.util.HashSet;
import java.util.UUID;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.environment.operators.InvalidOperationException;
import org.csvator.interpreter.parsingTable.function.FunctionValueInterface;
import org.csvator.interpreter.parsingTable.function.TypeMismatchException;
import org.csvator.interpreter.parsingTable.typeValues.BoolTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.SetTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.TypeValueInterface;

public class SetValue implements CollectionValueInterface {

	private String id;
	private HashSet<ValueInterface> value;

	public SetValue() {
		this.id = UUID.randomUUID().toString();
		this.value = new HashSet<ValueInterface>();
	}

	public SetValue(HashSet<ValueInterface> value) {
		this.id = UUID.randomUUID().toString();
		this.value = value;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public ValueInterface evaluate(Environment env) {
		return this;
	}

	public HashSet<ValueInterface> getSetValue() {
		return value;
	}

	@Override
	public String toString() {
		return this.value.toString();
	}

	@Override
	public TypeValueInterface getType() {
		return SetTypeValue.getInstance();
	}

	@Override
	public CollectionValueInterface concatAtHead(ValueInterface value) {
		if (value instanceof SetValue) {
			SetValue listVal = (SetValue) value;
			HashSet<ValueInterface> result = new HashSet<ValueInterface>();
			result.addAll(listVal.value);
			result.addAll(this.value);

			return new SetValue(result);
		}

		this.value.add(value);
		return this;
	}

	@Override
	public CollectionValueInterface concatAtTail(ValueInterface value) {
		return this.concatAtHead(value);
	}

	@Override
	public void remove(ValueInterface value) {
		// Do nothing because sets are not indexed
	}

	@Override
	public ValueInterface get(ValueInterface value) {
		return NullValue.getInstace();
	}

	@Override
	public ValueInterface contains(ValueInterface value) {
		if (this.value.contains(value)) {
			return new BooleanValue("true", true);
		}
		return new BooleanValue("false", false);
	}

	@Override
	public ValueInterface head() {
		throw new InvalidOperationException("Head is not a valid operation on a " + SetTypeValue.getInstance());
	}

	@Override
	public ValueInterface tail() {
		throw new InvalidOperationException("Tail is not a valid operation on a " + SetTypeValue.getInstance());
	}

	@Override
	public ValueInterface size() {
		return new IntegerValue(Integer.toString(this.value.size()), this.value.size());
	}

	@Override
	public CollectionValueInterface update(ValueInterface index, ValueInterface value) {
		throw new InvalidOperationException("Update is not a valid operation on a " + SetTypeValue.getInstance());
	}

	@Override
	public CollectionValueInterface swap(ValueInterface firstIndex, ValueInterface secondIndex) {
		throw new InvalidOperationException("Swap is not a valid operation on a " + SetTypeValue.getInstance());
	}

	@Override
	public CollectionValueInterface sort(FunctionValueInterface sortFunction) {
		throw new InvalidOperationException("Sort is not a valid operation on a " + SetTypeValue.getInstance());
	}

	@Override
	public CollectionValueInterface map(FunctionValueInterface mapFunction) {
		HashSet<ValueInterface> mappedValue = new HashSet<>();
		for (ValueInterface elem : value) {
			mappedValue.add(mapFunction.apply(elem));
		}

		SetValue mappedSet = new SetValue(mappedValue);
		return mappedSet;
	}

	@Override
	public CollectionValueInterface filter(FunctionValueInterface filterFunction) {
		HashSet<ValueInterface> filteredValue = new HashSet<>();
		for (ValueInterface elem : value) {
			ValueInterface result = filterFunction.apply(elem);

			if (! (result instanceof BooleanValue)) {
				throw new TypeMismatchException("Map function must return a " + BoolTypeValue.getInstance() + ". A " + result.getType() + " returned.");
			}

			BooleanValue filtered = (BooleanValue) result;
			if (filtered.getBooleanValue()) {
				filteredValue.add(elem);
			}
		}

		SetValue mappedSet = new SetValue(filteredValue);
		return mappedSet;
	}

	@Override
	public ValueInterface reduce(FunctionValueInterface reduceFunction, ValueInterface reduceValue) {
		ValueInterface reducedValue = reduceValue;
		for (ValueInterface elem : value) {
			reducedValue = reduceFunction.apply(elem, reducedValue);
		}
		return reducedValue;
	}

	@Override
	public String[][] buildTable() {
		String[][] table = new String[this.value.size()][];

		int i = 0;
		for (ValueInterface element : this.value) {
			table[i] = new String[1];
			if (element instanceof CollectionValueInterface) {
				table[i] = ((CollectionValueInterface) element).buildTableLine();
			} else {
				table[i][0] = element.toString();
			}
			i++;
		}

		return table;
	}

	@Override
	public String[] buildTableLine() {
		String[] line = new String[this.value.size()];

		int i = 0;
		for (ValueInterface element : this.value) {
			line[i] = element.toString();
			i++;
		}

		return line;
	}

	@Override
	public String[] buildTableHeader() {
		if (this.value.isEmpty()) {
			String[] emptyHeader = {"Col 1"};
			return emptyHeader;
		}
		ValueInterface first = this.value.iterator().next();
		if (first instanceof CollectionValueInterface) {
			return ((CollectionValueInterface) first).buildTableHeader();
		}

		String[] header = {"Col 1"};
		return header;
	}

	@Override
	public boolean universal(String variable, ValueInterface filter, Environment env) {
		Environment local = env.clone();
		boolean result = true;
		String varName = variable;

		for (ValueInterface elem : value) {
			local.putValue(varName, elem);
			BooleanValue filterResult = (BooleanValue) filter.evaluate(local);
			result = result && filterResult.getBooleanValue();
		}

		return result;
	}

	@Override
	public boolean existential(String variable, ValueInterface filter, Environment env) {
		Environment local = env.clone();
		String varName = variable;

		for (ValueInterface elem : value) {
			local.putValue(varName, elem);
			BooleanValue filterResult = (BooleanValue) filter.evaluate(local);
			if (filterResult.getBooleanValue()) {
				return true;
			}
		}

		return false;
	}

}
