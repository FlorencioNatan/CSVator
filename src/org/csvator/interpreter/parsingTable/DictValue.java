package org.csvator.interpreter.parsingTable;

import java.util.HashMap;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.environment.operators.InvalidOperationException;
import org.csvator.interpreter.parsingTable.function.TypeMismatchException;
import org.csvator.interpreter.parsingTable.typeValues.DictTypeValue;
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
		return DictTypeValue.getInstace();
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
		throw new InvalidOperationException("Head is not a valid operation on a " + DictTypeValue.getInstace());
	}

	@Override
	public ValueInterface tail() {
		throw new InvalidOperationException("Tail is not a valid operation on a " + DictTypeValue.getInstace());
	}

	@Override
	public ValueInterface size() {
		return new IntegerValue(Integer.toString(this.value.size()), this.value.size());
	}

}
