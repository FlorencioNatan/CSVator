package org.csvator.interpreter.parsingTable.function;

public class InvalidNumberOfParametersException extends RuntimeException {

	private static final long serialVersionUID = 3L;

	public InvalidNumberOfParametersException(String message) {
		super(message);
	}

}
