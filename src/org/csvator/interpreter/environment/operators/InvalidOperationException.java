package org.csvator.interpreter.environment.operators;

public class InvalidOperationException extends RuntimeException {

	private static final long serialVersionUID = 2L;

	public InvalidOperationException(String message) {
		super(message);
	}

}
