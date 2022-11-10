package org.csvator.interpreter.parsingTable.function;

import java.util.LinkedList;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.ExpressionValueInterface;
import org.csvator.interpreter.parsingTable.NullValue;
import org.csvator.interpreter.parsingTable.ValueInterface;
import org.csvator.interpreter.parsingTable.typeValues.TypeValueInterface;

public class AnonymousFunctionBody implements ValueInterface {

	private String id;
	private LinkedList<AnonymousFunctionBodyGuard> guards;
	private ExpressionValueInterface expression;

	public AnonymousFunctionBody(ExpressionValueInterface expression) {
		this.expression = expression;
	}

	public AnonymousFunctionBody(LinkedList<AnonymousFunctionBodyGuard> guards) {
		this.guards = guards;
	}

	public boolean isSingleExpression() {
		return expression != null;
	}

	public ExpressionValueInterface getExpression() {
		return expression;
	}

	public LinkedList<Guard> getGuards() {
		LinkedList<Guard> guards = new LinkedList<Guard>();
		for (AnonymousFunctionBodyGuard anonGuard : this.guards) {
			guards.add(anonGuard.toGuard());
		}
		return guards;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public ValueInterface evaluate(Environment env) {
		// TODO Auto-generated method stub
		if (expression != null) {
			return expression.evaluate(env);
		}
		for (AnonymousFunctionBodyGuard guard : guards) {
			ValueInterface guardValue = guard.evaluate(env);
			if (guardValue != NullValue.getInstace()) {
				return guardValue;
			}
		}
		return NullValue.getInstace();
	}

	@Override
	public TypeValueInterface getType() {
		// TODO Auto-generated method stub
		if (expression != null) {
			return expression.getType();
		}
		return guards.getFirst().getType();
	}

}
