package org.csvator.interpreter.parsingTable;

import java.util.LinkedList;

import org.csvator.interpreter.environment.Environment;
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
		return ListTypeValue.getInstace();
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

		this.value.addLast(value);
		return this;
	}

	@Override
	public void remove(ValueInterface value) {
		if (!(value instanceof IntegerValue)) {
			throw new TypeMismatchException("The index to be removed must of type " + IntTypeValue.getInstace());
		}
		this.value.remove(((IntegerValue) value).value);
	}

	@Override
	public ValueInterface get(ValueInterface value) {
		if (!(value instanceof IntegerValue)) {
			throw new TypeMismatchException("The index must of type " + IntTypeValue.getInstace());
		}
		return this.value.get(((IntegerValue) value).value);
	}

	@Override
	public ValueInterface contains(ValueInterface value) {
		if (this.value.contains(value)) {
			return new BooleanValue("true", true);
		}
		return new BooleanValue("false", false);
	}

}
