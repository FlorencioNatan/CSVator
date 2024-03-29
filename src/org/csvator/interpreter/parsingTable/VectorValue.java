package org.csvator.interpreter.parsingTable;

import java.util.UUID;
import java.util.Vector;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.function.FunctionValueInterface;
import org.csvator.interpreter.parsingTable.function.TypeMismatchException;
import org.csvator.interpreter.parsingTable.typeValues.BoolTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.IntTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.TypeValueInterface;
import org.csvator.interpreter.parsingTable.typeValues.VectorTypeValue;

public class VectorValue implements CollectionValueInterface {

	private String id;
	private Vector<ValueInterface> value;

	public VectorValue() {
		this.id = UUID.randomUUID().toString();
		this.value = new Vector<ValueInterface>();
	}

	public VectorValue(Vector<ValueInterface> value) {
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

			return new VectorValue(result);
		}

		if (value instanceof KeyValueExpressionValue) {
			KeyValueExpressionValue keyValue = (KeyValueExpressionValue) value;
			IntegerValue key = (IntegerValue) keyValue.getKey();
			this.value.add(key.getIntValue(), keyValue.getValue());
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

			return new VectorValue(result);
		}

		if (value instanceof KeyValueExpressionValue) {
			KeyValueExpressionValue keyValue = (KeyValueExpressionValue) value;
			IntegerValue key = (IntegerValue) keyValue.getKey();
			this.value.add(key.getIntValue(), keyValue.getValue());
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
		return this.value.get(0);
	}

	@Override
	public ValueInterface tail() {
		Vector<ValueInterface> result = new Vector<ValueInterface>();
		for (int i = 1; i < this.value.size(); i++) {
			ValueInterface element = this.value.get(i);
			result.add(element);
		}

		return new VectorValue(result);
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

	@Override
	public CollectionValueInterface map(FunctionValueInterface mapFunction) {
		Vector<ValueInterface> mappedValue = new Vector<>();
		for (ValueInterface elem : value) {
			mappedValue.add(mapFunction.apply(elem));
		}

		VectorValue mappedVec = new VectorValue(mappedValue);
		return mappedVec;
	}

	@Override
	public CollectionValueInterface filter(FunctionValueInterface filterFunction) {
		Vector<ValueInterface> filteredValue = new Vector<>();
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

		VectorValue filteredVec = new VectorValue(filteredValue);
		return filteredVec;
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
		ValueInterface first = this.value.firstElement();
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
