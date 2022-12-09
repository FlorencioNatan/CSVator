package org.csvator.interpreter.parsingTable;

public interface CollectionValueInterface extends ValueInterface {

	public CollectionValueInterface concatAtHead(ValueInterface value);

	public CollectionValueInterface concatAtTail(ValueInterface value);

	public void remove(ValueInterface value);

	public ValueInterface get(ValueInterface value);

	public ValueInterface contains(ValueInterface value);

}
