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
		return VectorTypeValue.getInstace();
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

		this.value.add(value);
		return this;
	}

	@Override
	public void remove(ValueInterface value) {
		if (!(value instanceof IntegerValue)) {
			throw new TypeMismatchException("The index to be removed must of type " + IntTypeValue.getInstace());
		}
		this.value.remove(((IntegerValue) value).value);
	}

}
