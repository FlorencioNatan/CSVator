package org.csvator.interpreter.parsingTable;

import java.util.HashMap;
import java.util.Map;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.environment.operators.InvalidOperationException;
import org.csvator.interpreter.parsingTable.function.FunctionValueInterface;
import org.csvator.interpreter.parsingTable.function.TypeMismatchException;
import org.csvator.interpreter.parsingTable.typeValues.BoolTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.DictTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.KeyValueTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.TypeValueInterface;

public class DictValue implements CollectionValueInterface {

	private String id;
	private HashMap<ValueInterface, ValueInterface> value;

	public DictValue(String id) {
		this.id = id;
		this.value = new HashMap<ValueInterface, ValueInterface>();
	}

	public DictValue(String id, HashMap<ValueInterface, ValueInterface> value) {
		this.id = id;
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

	public HashMap<ValueInterface, ValueInterface> getDictValue() {
		return value;
	}

	@Override
	public String toString() {
		return this.value.toString();
	}

	@Override
	public TypeValueInterface getType() {
		return DictTypeValue.getInstance();
	}

	@Override
	public CollectionValueInterface concatAtHead(ValueInterface value) {
		if (value instanceof DictValue) {
			DictValue vecVal = (DictValue) value;
			HashMap<ValueInterface, ValueInterface> result = new HashMap<ValueInterface, ValueInterface>();
			result.putAll(vecVal.value);
			result.putAll(this.value);

			return new DictValue(vecVal.id + this.id, result);
		}

		if (value instanceof KeyValueExpressionValue) {
			KeyValueExpressionValue keyValue = (KeyValueExpressionValue) value;
			this.value.put(keyValue.getKey(), keyValue.getValue());
			return this;
		}

		throw new TypeMismatchException("It's not possible to concatenate a dict with a " + value.getType());
	}

	@Override
	public CollectionValueInterface concatAtTail(ValueInterface value) {
		if (value instanceof DictValue) {
			DictValue vecVal = (DictValue) value;
			HashMap<ValueInterface, ValueInterface> result = new HashMap<ValueInterface, ValueInterface>();
			result.putAll(this.value);
			result.putAll(vecVal.value);

			return new DictValue(vecVal.id + this.id, result);
		}

		if (value instanceof KeyValueExpressionValue) {
			KeyValueExpressionValue keyValue = (KeyValueExpressionValue) value;
			this.value.put(keyValue.getKey(), keyValue.getValue());
			return this;
		}

		throw new TypeMismatchException("It's not possible to concatenate a dict with a " + value.getType());
	}

	@Override
	public void remove(ValueInterface value) {
		this.value.remove(value);
	}

	@Override
	public ValueInterface get(ValueInterface value) {
		return this.value.get(value);
	}

	@Override
	public ValueInterface contains(ValueInterface value) {
		if (this.value.containsKey(value)) {
			return new BooleanValue("true", true);
		}
		return new BooleanValue("false", false);
	}

	@Override
	public ValueInterface head() {
		throw new InvalidOperationException("Head is not a valid operation on a " + DictTypeValue.getInstance());
	}

	@Override
	public ValueInterface tail() {
		throw new InvalidOperationException("Tail is not a valid operation on a " + DictTypeValue.getInstance());
	}

	@Override
	public ValueInterface size() {
		return new IntegerValue(Integer.toString(this.value.size()), this.value.size());
	}

	@Override
	public CollectionValueInterface update(ValueInterface index, ValueInterface value) {
		this.value.put(index, value);
		return this;
	}

	@Override
	public CollectionValueInterface swap(ValueInterface firstIndex, ValueInterface secondIndex) {
		ValueInterface temp = this.value.get(firstIndex);
		this.value.put(firstIndex, this.value.get(secondIndex));
		this.value.put(secondIndex, temp);
		return this;
	}

	@Override
	public CollectionValueInterface sort(FunctionValueInterface sortFunction) {
		throw new InvalidOperationException("Sort is not a valid operation on a " + DictTypeValue.getInstance());
	}

	@Override
	public CollectionValueInterface map(FunctionValueInterface mapFunction) {
		HashMap<ValueInterface, ValueInterface> mappedValue = new HashMap<>();

		for (Map.Entry<ValueInterface, ValueInterface> set : value.entrySet()) {
			ValueInterface result = mapFunction.apply(set.getKey(), set.getValue());

			if (! (result instanceof KeyValueExpressionValue)) {
				throw new TypeMismatchException("Map function must return a " + KeyValueTypeValue.getInstance() + ". A " + result.getType() + " returned.");
			}

			KeyValueExpressionValue keyValue = (KeyValueExpressionValue) result;
			mappedValue.put(keyValue.getKey(), keyValue.getValue());
		}

		DictValue mappedDict = new DictValue(id, mappedValue);
		return mappedDict;
	}

	@Override
	public CollectionValueInterface filter(FunctionValueInterface filterFunction) {
		HashMap<ValueInterface, ValueInterface> filteredValue = new HashMap<>();

		for (Map.Entry<ValueInterface, ValueInterface> set : value.entrySet()) {
			ValueInterface result = filterFunction.apply(set.getKey(), set.getValue());

			if (! (result instanceof BooleanValue)) {
				throw new TypeMismatchException("Map function must return a " + BoolTypeValue.getInstance() + ". A " + result.getType() + " returned.");
			}

			BooleanValue filtered = (BooleanValue) result;
			if (filtered.getBooleanValue()) {
				filteredValue.put(set.getKey(), set.getValue());
			}
		}

		DictValue mappedDict = new DictValue(id, filteredValue);
		return mappedDict;
	}

	@Override
	public ValueInterface reduce(FunctionValueInterface reduceFunction, ValueInterface reduceValue) {
		ValueInterface reducedValue = reduceValue;
		for (Map.Entry<ValueInterface, ValueInterface> set : value.entrySet()) {
			reducedValue = reduceFunction.apply(set.getKey(), set.getValue(), reducedValue);
		}
		return reducedValue;
	}

	@Override
	public String[][] buildTable() {
		String[][] table = new String[1][];

		int i = 0;
		table[i] = this.buildTableLine();

		return table;
	}

	@Override
	public String[] buildTableLine() {
		String[] line = new String[this.value.size()];

		int i = 0;
		for (ValueInterface element : this.value.values()) {
			line[i] = element.toString();
			i++;
		}

		return line;
	}

	@Override
	public String[] buildTableHeader() {
		String[] header = new String[this.value.size()];

		int i = 0;
		for (ValueInterface element : this.value.keySet()) {
			header[i] = element.toString();
			i++;
		}

		return header;
	}

	@Override
	public boolean universal(String variable, ValueInterface filter, Environment env) {
		Environment local = env.clone();
		boolean result = true;
		String varName = variable;

		for (Map.Entry<ValueInterface, ValueInterface> set : value.entrySet()) {
			KeyValueExpressionValue elem = new KeyValueExpressionValue("elem", set.getKey(), set.getValue());
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

		for (Map.Entry<ValueInterface, ValueInterface> set : value.entrySet()) {
			KeyValueExpressionValue elem = new KeyValueExpressionValue("elem", set.getKey(), set.getValue());
			local.putValue(varName, elem);
			BooleanValue filterResult = (BooleanValue) filter.evaluate(local);
			if (filterResult.getBooleanValue()) {
				return true;
			}
		}

		return false;
	}

}
