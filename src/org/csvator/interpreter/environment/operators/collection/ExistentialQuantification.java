package org.csvator.interpreter.environment.operators.collection;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.BooleanValue;
import org.csvator.interpreter.parsingTable.CollectionValueInterface;
import org.csvator.interpreter.parsingTable.ValueInterface;
import org.csvator.interpreter.parsingTable.typeValues.BoolTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.TypeValueInterface;

public class ExistentialQuantification implements ValueInterface {

	private String variable;
	private ValueInterface collection;
	private ValueInterface filter;

	public ExistentialQuantification(String variable, ValueInterface collection, ValueInterface filter) {
		this.variable = variable;
		this.collection = collection;
		this.filter = filter;
	}

	@Override
	public String getId() {
		return "Existential quantification";
	}

	@Override
	public ValueInterface evaluate(Environment env) {
		collection = collection.evaluate(env);
		if (collection instanceof CollectionValueInterface) {
			CollectionValueInterface col = (CollectionValueInterface) collection;
			return new BooleanValue("result", col.existential(variable, filter, env));
		}
		return new BooleanValue("false", false);
	}

	@Override
	public TypeValueInterface getType() {
		return BoolTypeValue.getInstance();
	}

}
