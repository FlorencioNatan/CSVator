package org.csvator.interpreter.parsingTable.typeValues;

import java.security.InvalidParameterException;
import java.util.LinkedList;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.DoubleValue;
import org.csvator.interpreter.parsingTable.FieldValue;
import org.csvator.interpreter.parsingTable.IntegerValue;
import org.csvator.interpreter.parsingTable.KeyValueExpressionValue;
import org.csvator.interpreter.parsingTable.RecordValue;
import org.csvator.interpreter.parsingTable.StringValue;
import org.csvator.interpreter.parsingTable.ValueInterface;
import org.csvator.interpreter.parsingTable.function.FunctionValueInterface;
import org.csvator.interpreter.parsingTable.function.InvalidNumberOfParametersException;
import org.csvator.interpreter.parsingTable.function.TypeMismatchException;
import org.csvator.interpreter.parsingTable.function.UserDefinedFunctionValue;

public class RecordTypeValue implements TypeValueInterface, FunctionValueInterface {

	private String id;
	private LinkedList<FieldValue> fields;
	private LinkedList<ValueInterface> invariants;

	public RecordTypeValue(LinkedList<FieldValue> fields) {
		this.fields = fields;
		this.invariants = new LinkedList<ValueInterface>();
	}

	public void setInvariants(LinkedList<ValueInterface> invariants) {
		this.invariants = invariants;
	}

	public LinkedList<FieldValue> getFields() {
		return fields;
	}

	public LinkedList<ValueInterface> getInvariants() {
		return invariants;
	}

	@Override
	public String getId() {
		return "record " + this.id;
	}

	public String setId(String id) {
		return this.id = id;
	}

	@Override
	public ValueInterface evaluate(Environment env) {
		return this;
	}

	@Override
	public TypeValueInterface getType() {
		return this;
	}

	@Override
	public boolean equalsToType(TypeValueInterface type) {
		if (type instanceof AnyTypeValue) {
			return true;
		}

		if (type instanceof CollectionTypeValue) {
			return true;
		}

		if (type instanceof RecordTypeValue) {
			return this.compareToRecordType((RecordTypeValue) type);
		}

		return false;
	}

	private boolean compareToRecordType(RecordTypeValue type) {
		if (this.fields.size() != type.fields.size()) {
			return false;
		}

		for (int i = 0; i < this.fields.size(); i++) {
			if (this.fields.get(i) != type.fields.get(i)) {
				return false;
			}
		}

		return true;
	}

	@Override
	public Environment createLocalEnvironment(LinkedList<ValueInterface> values, Environment father) {
		if (values.size() != fields.size()) {
			throw new InvalidNumberOfParametersException("The record " + this.id.trim() + " expects "
				+ fields.size() + " parameters, but " + values.size() +  " are found.");
		}
		Environment local = new Environment();
		local.setFatherEnvironment(father);
		for (int i = 0; i < fields.size(); i++) {
			ValueInterface parameterValue = values.get(i);
			if (fields.get(i).getType().getClass() != FunctionTypeValue.class) {
				parameterValue = parameterValue.evaluate(father);
			}

			if (fields.get(i).getType().getClass() == FunctionTypeValue.class) {
				try {
					parameterValue = parameterValue.evaluate(father);

					UserDefinedFunctionValue paramenterFunction = (UserDefinedFunctionValue) this.extractFunctionFromParameter(parameterValue, father);
					parameterValue = paramenterFunction;
				} catch (InvalidParameterException exception) {
					// do nothing
				}
			}

			if (fields.get(i).getType() == DoubleTypeValue.getInstance() && parameterValue.getType() == IntTypeValue.getInstance()) {
				int parameterContent = ((IntegerValue) parameterValue).getIntValue();
				parameterValue = new DoubleValue(parameterValue.getId(), parameterContent);
			}

			if (!fields.get(i).getType().equalsToType(parameterValue.getType())) {
				throw new TypeMismatchException("Type mismatch on record " + this.id.trim() + " parameter " + (i + 1) + ". Expected " + fields.get(i).getType() + " found " + parameterValue.getType());
			}
			local.putValue(fields.get(i).getIdVariable(), parameterValue);
		}

		return local;
	}

	private UserDefinedFunctionValue extractFunctionFromParameter(ValueInterface parameterValue, Environment father) throws InvalidParameterException {
		if (parameterValue.getType().getClass() == FunctionTypeValue.class) {
			return (UserDefinedFunctionValue) parameterValue;
		}
		if (parameterValue.getType() == VariableTypeValue.getInstance()) {
			return (UserDefinedFunctionValue) father.getValueOf(parameterValue.getId());
		}
		throw new InvalidParameterException("The parameter is not a function");
	}

	@Override
	public TypeValueInterface getReturnType() {
		return this;
	}

	@Override
	public ValueInterface apply(Environment env) {
		RecordValue record = new RecordValue(id, fields);
		record.setType(this);

		for (FieldValue fieldValue : fields) {
			String fieldVariable = fieldValue.getIdVariable();
			StringValue key = new StringValue(fieldVariable, fieldVariable);
			ValueInterface value = env.getValueOf(fieldVariable);
			KeyValueExpressionValue keyValue = new KeyValueExpressionValue(fieldVariable, key, value);
			record.concatAtTail(keyValue);
		}

		return record;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();

		str.append("record " + this.id + "\n");
		str.append("\tfields\n");

		for (FieldValue fieldValue : fields) {
			str.append("\t\t" + fieldValue + "\n");
		}

		str.append("\tend\n");
		str.append("\tinvariants\n");

		for (ValueInterface value : invariants) {
			str.append("\t\t" + value + "\n");
		}

		str.append("\tend\n");
		str.append("end\n");

		return str.toString();
	}

	@Override
	public ValueInterface createValue(String strValue) {
		return null;
	}

}
