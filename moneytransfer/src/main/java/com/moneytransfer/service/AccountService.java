package com.moneytransfer.service;

import java.sql.Connection;

import com.moneytransfer.exception.ExceptionWrapper;
import com.moneytransfer.repository.AccountRepository;
import com.moneytransfer.repository.DatabaseConnection;
import com.moneytransfer.wrapper.Account;

/**
 * This class use to serve all the request like Add, Update and Fetch Account information.
 * @author Vikas Gupta
 *
 */
public class AccountService implements IAccountService {

	private AccountRepository accountRepository = new AccountRepository();
	
	@Override
	public int addAccount(Account account) throws ExceptionWrapper {
		return accountRepository.addAccount(account);
	}

	@Override
	public Account fetchAccount(Connection connection, int accountNumber) throws ExceptionWrapper {
		if(connection == null) connection = DatabaseConnection.getInstance().getConnection();
		return accountRepository.fetchAccount(connection, accountNumber);
	}

	@Override
	public double addAmount(Connection connection, Account accountTo, double amountToAdd) throws ExceptionWrapper {
		double availableAmoun = accountTo.getAmount() + amountToAdd;
		accountTo.setAmount(availableAmoun);
		accountRepository.updateAccount(connection, accountTo);
		return accountTo.getAmount();
	}

	@Override
	public double withdrawAmount(Connection connection, Account accountFrom, double amountToTake) throws ExceptionWrapper {
		if (hasEnaughAmount(accountFrom.getAmount(), amountToTake)) {
			accountFrom.setAmount(accountFrom.getAmount() - amountToTake);
			accountRepository.updateAccount(connection, accountFrom);
		}else {
			throw new ExceptionWrapper("Account Number - " + accountFrom.getAccountNumber() + " do not have sufficient amout to transfer.");
		}
		return accountFrom.getAmount();
	}

	boolean hasEnaughAmount(double initialAmount, double amountToTake) {
		return initialAmount >= amountToTake;
	}
	
	public AccountService() {
		
	}
	
	public AccountService(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}
}
