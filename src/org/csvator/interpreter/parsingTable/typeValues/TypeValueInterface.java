package org.csvator.interpreter.parsingTable.typeValues;

import org.csvator.interpreter.parsingTable.ValueInterface;

public interface TypeValueInterface extends ValueInterface {

	boolean equalsToType(TypeValueInterface type);

	ValueInterface createValue(String strValue);

}
