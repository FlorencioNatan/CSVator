package org.csvator.interpreter.parsingTable;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.LinkedList;

import org.csvator.interpreter.environment.operators.InvalidOperationException;
import org.csvator.interpreter.parsingTable.function.TypeMismatchException;
import org.csvator.interpreter.parsingTable.typeValues.RecordTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.StringTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.TypeValueInterface;

public class RecordValue extends DictValue {

	private LinkedList<FieldValue> fields;
	private HashMap<String, FieldValue> fieldsMap;
	private RecordTypeValue type;

	public RecordValue(String id) {
		super(id);
		throw new InvalidOperationException("Its not possible create a record with a custom HashMap.");
	}

	public RecordValue(String id, HashMap<ValueInterface, ValueInterface> value) {
		super(id);
		throw new InvalidOperationException("Its not possible create a record with a custom HashMap.");
	}

	public RecordValue(String id, LinkedList<FieldValue> fields) {
		super(id);
		this.fields = fields;
		this.fieldsMap = new HashMap<String, FieldValue>();

		for (FieldValue fieldValue : fields) {
			this.fieldsMap.put(fieldValue.getIdVariable(), fieldValue);
		}
	}

	@Override
	public CollectionValueInterface concatAtHead(ValueInterface value) {
		if (value instanceof KeyValueExpressionValue) {
			this.validateField(value);
			return super.concatAtHead(value);
		}

		throw new TypeMismatchException("It's not possible to concatenate a record with a " + value.getType());
	}

	@Override
	public CollectionValueInterface concatAtTail(ValueInterface value) {
		if (value instanceof KeyValueExpressionValue) {
			this.validateField(value);
			return super.concatAtTail(value);
		}

		throw new TypeMismatchException("It's not possible to concatenate a record with a " + value.getType());
	}

	@Override
	public void remove(ValueInterface value) {
		throw new InvalidOperationException("Remove is not a valid operation on a record.");
	}

	@Override
	public CollectionValueInterface update(ValueInterface index, ValueInterface value) {
		this.validateField(index, value);
		return super.update(index, value);
	}

	@Override
	public CollectionValueInterface swap(ValueInterface firstIndex, ValueInterface secondIndex) {
		ValueInterface firstValue = this.get(firstIndex);
		ValueInterface secondValue = this.get(secondIndex);
		this.validateField(firstIndex, firstValue);
		this.validateField(secondIndex, secondValue);
		return super.swap(firstIndex, secondIndex);
	}

	private void validateField(ValueInterface value) {
		KeyValueExpressionValue keyValue = (KeyValueExpressionValue) value;
		this.validateField(keyValue.getKey(), keyValue.getValue());
	}

	private void validateField(ValueInterface index, ValueInterface value) {
		if (!(index instanceof StringValue)) {
			throw new TypeMismatchException("The key must be a " + StringTypeValue.getInstance() + ".");
		}
		StringValue key = (StringValue) index;
		if (!this.fieldsMap.containsKey(key.getStrValue())) {
			throw new InvalidParameterException("The field " + key.getStrValue() + "doesn't exist on record " + this.getId());
		}
		FieldValue field = this.fieldsMap.get(key.getStrValue());
		if (!field.getType().equalsToType(value.getType())) {
			throw new TypeMismatchException("The of the field must be " + field.getType() + " " + value.getType() + " found.");
		}
	}

	public void setType(RecordTypeValue type) {
		this.type = type;
	}

	@Override
	public TypeValueInterface getType() {
		return this.type;
	}

}
