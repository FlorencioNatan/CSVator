package org.csvator.interpreter.parsingTable;

public interface CollectionValueInterface extends ValueInterface {

	public CollectionValueInterface concatAtHead(ValueInterface value);

	public CollectionValueInterface concatAtTail(ValueInterface value);

}
