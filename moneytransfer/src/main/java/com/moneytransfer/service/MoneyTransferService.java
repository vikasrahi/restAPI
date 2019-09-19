package com.moneytransfer.service;

import java.sql.Connection;
import java.sql.SQLException;

import com.moneytransfer.exception.ExceptionWrapper;
import com.moneytransfer.repository.DatabaseConnection;
import com.moneytransfer.wrapper.Account;
import com.moneytransfer.wrapper.MoneyTransfer;

/**
 * This class will used for transfer amount between accounts.
 * @author vgupta
 *
 */
public class MoneyTransferService implements IMoneyTransferService {

	private AccountService accountService = new AccountService();

	@Override
	public boolean transferAmount(MoneyTransfer moneyTransfer) throws ExceptionWrapper {
			Connection connection = DatabaseConnection.getInstance().getConnection();
			return tranferAmountWithTransactionalBoundry(moneyTransfer, connection);
	}

	/**
	 * This method use for transfer money by passing account information with in transaction.
	 * @param moneyTransfer
	 * @param connection
	 * @return
	 * @throws ExceptionWrapper
	 */
	private boolean tranferAmountWithTransactionalBoundry(MoneyTransfer moneyTransfer, Connection connection) throws ExceptionWrapper {
		try {
			connection.setAutoCommit(false);
			Account accountFrom = accountService.fetchAccount(connection, moneyTransfer.getAccountNumberFrom());
			Account accountTo = accountService.fetchAccount(connection, moneyTransfer.getAccountNumberTo());
			accountService.withdrawAmount(connection, accountFrom, moneyTransfer.getAmount());
			accountService.addAmount(connection, accountTo, moneyTransfer.getAmount());
			connection.commit();
		} catch (Exception ex) {
			try {
				connection.rollback();
			} catch (SQLException e) {
				throw new ExceptionWrapper("Can't transfer money due to : " + e.getMessage());
			}
			throw new ExceptionWrapper(ex.getMessage());
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
			}
		}
		return true;
	}

}
