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
			super.concatAtHead(value);
			this.type.checkInvariants(getDictValue());
			return this;
		}

		throw new TypeMismatchException("It's not possible to concatenate a record with a " + value.getType());
	}

	@Override
	public CollectionValueInterface concatAtTail(ValueInterface value) {
		if (value instanceof KeyValueExpressionValue) {
			this.validateField(value);
			super.concatAtTail(value);
			this.type.checkInvariants(getDictValue());
			return this;
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
		super.update(index, value);
		this.type.checkInvariants(getDictValue());
		return this;
	}

	@Override
	public CollectionValueInterface swap(ValueInterface firstIndex, ValueInterface secondIndex) {
		ValueInterface firstValue = this.get(firstIndex);
		ValueInterface secondValue = this.get(secondIndex);
		this.validateField(firstIndex, firstValue);
		this.validateField(secondIndex, secondValue);
		super.swap(firstIndex, secondIndex);
		this.type.checkInvariants(getDictValue());
		return this;
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

	@Override
	public String[][] buildTable() {
		String[][] table = new String[this.fields.size()][];

		int i = 0;
		for (FieldValue field : this.fields) {
			String idVariable = field.getIdVariable();
			StringValue fieldKey = new StringValue(idVariable, idVariable);
			ValueInterface fieldValue = this.get(fieldKey);

			table[i] = new String[1];
			if (fieldValue instanceof CollectionValueInterface) {
				table[i] = ((CollectionValueInterface) fieldValue).buildTableLine();
			} else {
				table[i][0] = fieldValue.toString();
			}
			i++;
		}

		return table;
	}

	@Override
	public String[] buildTableLine() {
		String[] line = new String[this.fields.size()];

		int i = 0;
		for (FieldValue field : this.fields) {
			String idVariable = field.getIdVariable();
			StringValue fieldKey = new StringValue(idVariable, idVariable);
			ValueInterface fieldValue = this.get(fieldKey);

			line[i] = fieldValue.toString();
			i++;
		}

		return line;
	}

	@Override
	public String[] buildTableHeader() {
		String[] line = new String[this.fields.size()];

		int i = 0;
		for (FieldValue field : this.fields) {
			line[i] = field.getIdVariable();
			i++;
		}

		return line;
	}

}
