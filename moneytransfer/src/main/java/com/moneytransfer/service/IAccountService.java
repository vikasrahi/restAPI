package com.moneytransfer.service;

import java.sql.Connection;

import com.moneytransfer.exception.ExceptionWrapper;
import com.moneytransfer.wrapper.Account;

/**
 * This interface define all methods like Add, Update and Fetch Account information.
 * @author Vikas Gupta
 *
 */
public interface IAccountService {
	/**
	 * This Method will be used to create new account with given account information.
	 * @param account
	 * @return
	 * @throws ExceptionWrapper
	 */
	int addAccount(Account account) throws ExceptionWrapper;
	
	/**
	 * This Method will be used to fetch a account with given account number.
	 * @param connection
	 * @param accountNumber
	 * @return
	 * @throws ExceptionWrapper
	 */
    Account fetchAccount(Connection connection, int accountNumber) throws ExceptionWrapper;
    
    /**
     * This Method will be used to update account information.
     * @param connection
     * @param account
     * @param amountToAdd
     * @return
     * @throws ExceptionWrapper
     */
    double addAmount(Connection connection, Account account, double amountToAdd) throws ExceptionWrapper;
    
    /**
     * This Method will be used to withdraw amount from account by given account information.
     * @param connection
     * @param account
     * @param amountToTake
     * @return
     * @throws ExceptionWrapper
     */
    double withdrawAmount(Connection connection, Account account ,double amountToTake) throws ExceptionWrapper;
    
}
