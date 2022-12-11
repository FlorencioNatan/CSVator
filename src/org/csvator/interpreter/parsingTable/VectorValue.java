package org.csvator.interpreter.parsingTable;

import java.util.Vector;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.function.TypeMismatchException;
import org.csvator.interpreter.parsingTable.typeValues.IntTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.TypeValueInterface;
import org.csvator.interpreter.parsingTable.typeValues.VectorTypeValue;

public class VectorValue implements CollectionValueInterface {

	private String id;
	private Vector<ValueInterface> value;

	public VectorValue(String id) {
		this.id = id;
		this.value = new Vector<ValueInterface>();
	}

	public VectorValue(String id, Vector<ValueInterface> value) {
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

	public Vector<ValueInterface> getVectorValue() {
		return value;
	}

	@Override
	public String toString() {
		return this.value.toString();
	}

	@Override
	public TypeValueInterface getType() {
		return VectorTypeValue.getInstance();
	}

	@Override
	public CollectionValueInterface concatAtHead(ValueInterface value) {
		if (value instanceof VectorValue) {
			VectorValue vecVal = (VectorValue) value;
			Vector<ValueInterface> result = new Vector<ValueInterface>();
			result.addAll(vecVal.value);
			result.addAll(this.value);

			return new VectorValue(vecVal.id + this.id, result);
		}

		if (value instanceof KeyValueExpressionValue) {
			KeyValueExpressionValue keyValue = (KeyValueExpressionValue) value;
			IntegerValue key = (IntegerValue) keyValue.getKey();
			this.value.add(key.value, keyValue.getValue());
			return this;
		}

		this.value.add(0, value);
		return this;
	}

	@Override
	public CollectionValueInterface concatAtTail(ValueInterface value) {
		if (value instanceof VectorValue) {
			VectorValue vecVal = (VectorValue) value;
			Vector<ValueInterface> result = new Vector<ValueInterface>();
			result.addAll(this.value);
			result.addAll(vecVal.value);

			return new VectorValue(vecVal.id + this.id, result);
		}

		if (value instanceof KeyValueExpressionValue) {
			KeyValueExpressionValue keyValue = (KeyValueExpressionValue) value;
			IntegerValue key = (IntegerValue) keyValue.getKey();
			this.value.add(key.value, keyValue.getValue());
			return this;
		}

		this.value.add(value);
		return this;
	}

	@Override
	public void remove(ValueInterface value) {
		if (!(value instanceof IntegerValue)) {
			throw new TypeMismatchException("The index to be removed must of type " + IntTypeValue.getInstance());
		}
		this.value.remove(((IntegerValue) value).value);
	}

	@Override
	public ValueInterface get(ValueInterface value) {
		if (!(value instanceof IntegerValue)) {
			throw new TypeMismatchException("The index must of type " + IntTypeValue.getInstance());
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

	@Override
	public ValueInterface head() {
		return this.value.get(0);
	}

	@Override
	public ValueInterface tail() {
		Vector<ValueInterface> result = new Vector<ValueInterface>();
		for (int i = 1; i < this.value.size(); i++) {
			ValueInterface element = this.value.get(i);
			result.add(element);
		}

		return new VectorValue(this.id, result);
	}

	@Override
	public ValueInterface size() {
		return new IntegerValue(Integer.toString(this.value.size()), this.value.size());
	}

}
