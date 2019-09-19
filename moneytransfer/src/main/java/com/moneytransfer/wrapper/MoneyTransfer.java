package com.moneytransfer.wrapper;

/**
 * This POJO class contains all account information for tranfer money.
 * @author Vikas Gupta
 *
 */
public class MoneyTransfer {
	
	private int accountNumberFrom;
	private int accountNumberTo;
	private double amount;

	public int getAccountNumberFrom() {
		return accountNumberFrom;
	}

	public void setAccountNumberFrom(int accountNumberFrom) {
		this.accountNumberFrom = accountNumberFrom;
	}

	public int getAccountNumberTo() {
		return accountNumberTo;
	}

	public void setAccountNumberTo(int accountNumberTo) {
		this.accountNumberTo = accountNumberTo;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
}
