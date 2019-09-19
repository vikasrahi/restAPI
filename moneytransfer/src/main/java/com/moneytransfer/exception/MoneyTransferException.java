package com.moneytransfer.exception;

/**
 * This Customer Exception Class use for sending exception message and cause of exception to End user.
 * @author Vikas Gupta
 *
 */
public class MoneyTransferException extends Exception{

	private static final long serialVersionUID = -8465911424078838020L;

	public MoneyTransferException(String message, Throwable cause) {
		super(message, cause);
	}

}
