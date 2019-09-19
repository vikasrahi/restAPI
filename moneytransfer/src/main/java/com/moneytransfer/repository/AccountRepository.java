package com.moneytransfer.repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

import com.moneytransfer.exception.ExceptionWrapper;
import com.moneytransfer.wrapper.Account;

/**
 * This call will be used for database operation like insert,fetch and update and
 * after completion close resultSet, statement and connection object.
 * @author Vikas Gupta
 *
 */
public class AccountRepository {
	
	/**
	 * This method will be used for insert data in Account table.
	 * @param account
	 * @return
	 * @throws ExceptionWrapper
	 */
	public int addAccount(Account account) throws ExceptionWrapper {
		int accountNumber = new Random().nextInt(9999);
		Statement stmt = null;
		try {
			Connection conn = DatabaseConnection.getInstance().getConnection();
			conn.setAutoCommit(false);
			
			try {
				stmt = conn.createStatement();
				String sql = "INSERT INTO ACCOUNT " + "VALUES (" + accountNumber + ",'" + account.getName() + "',"
						+ account.getAmount() + ")";
				stmt.executeUpdate(sql);
				
			} catch (Exception ex) {
				conn.rollback();
			} finally {
				stmt.close();
			}
			
			conn.commit();
			conn.close();
		} catch (SQLException e) {
			throw new ExceptionWrapper("Account has not been Created with reason : " + e.getMessage());
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			} // nothing we can do
		}
		System.out.println(accountNumber);
		return accountNumber;
	}

	/**
	 * This method will be used for update the data of Account table.
	 * @param connection
	 * @param account
	 * @throws ExceptionWrapper
	 */
	public void updateAccount(Connection connection, Account account) throws ExceptionWrapper {
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
			String sql = "UPDATE ACCOUNT SET amount = " + account.getAmount() + "where name = '" + account.getName()
					+ "';";
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (SQLException e) {
			throw new ExceptionWrapper("Account updation has been failed due to : " +  e.getMessage());
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			} // nothing we can do
		}

	}

	/**
	 * This method will be used to get the details from Account Table.
	 * @param connection
	 * @param accountNumber
	 * @return
	 * @throws ExceptionWrapper
	 */
	public Account fetchAccount(Connection connection, int accountNumber) throws ExceptionWrapper {
		Account account = null;
		Statement stmt = null;
		try {
			stmt = connection.createStatement();
			String sql = "SELECT id, name, amount FROM ACCOUNT where id = " + accountNumber + ";";
			ResultSet rs = stmt.executeQuery(sql);

			if (rs.next() == false) {
				throw new ExceptionWrapper("Account not found with Account Number : " + accountNumber);
			} else {
				do {
					// Retrieve by column name
					int id = rs.getInt("id");
					double amount = rs.getDouble("amount");
					String name = rs.getString("name");
					account = new Account();
					account.setAmount(amount);
					account.setName(name);
					account.setAccountNumber(id);
					
				} while (rs.next());
			}

			// STEP 4: Clean-up environment
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			throw new ExceptionWrapper("Fetched Account has been failed due to : "+ e.getMessage());
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			} // nothing we can do
		}
		return account;
	}
}
