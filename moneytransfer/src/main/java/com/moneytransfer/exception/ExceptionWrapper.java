package com.moneytransfer.exception;

/**
 * This Customer Exception Class use for sending exception message to End user.
 * @author vgupta
 *
 */
public class ExceptionWrapper extends Exception {
	
	private static final long serialVersionUID = 8700995085194314167L;

	public ExceptionWrapper(String message) {
		super(message);
	}
}