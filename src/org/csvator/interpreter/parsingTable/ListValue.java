package org.csvator.interpreter.parsingTable;

import java.util.LinkedList;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.function.FunctionValueInterface;
import org.csvator.interpreter.parsingTable.function.TypeMismatchException;
import org.csvator.interpreter.parsingTable.typeValues.IntTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.ListTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.TypeValueInterface;

public class ListValue implements CollectionValueInterface {

	private String id;
	private LinkedList<ValueInterface> value;

	public ListValue(String id) {
		this.id = id;
		this.value = new LinkedList<ValueInterface>();
	}

	public ListValue(String id, LinkedList<ValueInterface> value) {
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

	public LinkedList<ValueInterface> getListValue() {
		return value;
	}

	@Override
	public String toString() {
		return this.value.toString();
	}

	@Override
	public TypeValueInterface getType() {
		return ListTypeValue.getInstance();
	}

	@Override
	public CollectionValueInterface concatAtHead(ValueInterface value) {
		if (value instanceof ListValue) {
			ListValue listVal = (ListValue) value;
			LinkedList<ValueInterface> result = new LinkedList<ValueInterface>();
			result.addAll(listVal.value);
			result.addAll(this.value);

			return new ListValue(listVal.id + this.id, result);
		}

		if (value instanceof KeyValueExpressionValue) {
			KeyValueExpressionValue keyValue = (KeyValueExpressionValue) value;
			IntegerValue key = (IntegerValue) keyValue.getKey();
			this.value.add(key.getIntValue(), keyValue.getValue());
			return this;
		}

		this.value.addFirst(value);
		return this;
	}

	@Override
	public CollectionValueInterface concatAtTail(ValueInterface value) {
		if (value instanceof ListValue) {
			ListValue listVal = (ListValue) value;
			LinkedList<ValueInterface> result = new LinkedList<ValueInterface>();
			result.addAll(this.value);
			result.addAll(listVal.value);

			return new ListValue(listVal.id + this.id, result);
		}

		if (value instanceof KeyValueExpressionValue) {
			KeyValueExpressionValue keyValue = (KeyValueExpressionValue) value;
			IntegerValue key = (IntegerValue) keyValue.getKey();
			this.value.add(key.getIntValue(), keyValue.getValue());
			return this;
		}

		this.value.addLast(value);
		return this;
	}

	@Override
	public void remove(ValueInterface value) {
		if (!(value instanceof IntegerValue)) {
			throw new TypeMismatchException("The index to be removed must of type " + IntTypeValue.getInstance());
		}
		this.value.remove(((IntegerValue) value).getIntValue());
	}

	@Override
	public ValueInterface get(ValueInterface value) {
		if (!(value instanceof IntegerValue)) {
			throw new TypeMismatchException("The index must of type " + IntTypeValue.getInstance());
		}
		return this.value.get(((IntegerValue) value).getIntValue());
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
		return this.value.getFirst();
	}

	@Override
	public ValueInterface tail() {
		LinkedList<ValueInterface> result = new LinkedList<ValueInterface>();
		for (int i = 1; i < this.value.size(); i++) {
			ValueInterface element = this.value.get(i);
			result.add(element);
		}

		return new ListValue(this.id, result);
	}

	@Override
	public ValueInterface size() {
		return new IntegerValue(Integer.toString(this.value.size()), this.value.size());
	}

	@Override
	public CollectionValueInterface update(ValueInterface index, ValueInterface value) {
		if (!(index instanceof IntegerValue)) {
			throw new TypeMismatchException("The index must of type " + IntTypeValue.getInstance());
		}

		this.value.set(((IntegerValue) index).getIntValue(), value);
		return this;
	}

	@Override
	public CollectionValueInterface swap(ValueInterface firstIndex, ValueInterface secondIndex) {
		if (!(firstIndex instanceof IntegerValue)) {
			throw new TypeMismatchException("The firstIndex must of type " + IntTypeValue.getInstance());
		}

		if (!(secondIndex instanceof IntegerValue)) {
			throw new TypeMismatchException("The secondIndex must of type " + IntTypeValue.getInstance());
		}

		int fIndex = ((IntegerValue) firstIndex).getIntValue();
		int sIndex = ((IntegerValue) secondIndex).getIntValue();
		ValueInterface temp = this.value.get(fIndex);

		this.value.set(fIndex, this.value.get(sIndex));
		this.value.set(sIndex, temp);
		return this;
	}

	@Override
	public CollectionValueInterface sort(FunctionValueInterface sortFunction) {
		this.value.sort((valueA, valueB) -> {
			ValueInterface result = sortFunction.apply(valueA, valueB);

			if (! (result instanceof IntegerValue)) {
				throw new TypeMismatchException("Sort function must return a " + IntTypeValue.getInstance() + ". A " + result.getType() + " returned.");
			}

			return ((IntegerValue) result).getIntValue();
		});
		return this;
	}

}
