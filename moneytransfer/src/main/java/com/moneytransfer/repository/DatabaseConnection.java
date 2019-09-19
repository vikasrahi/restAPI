package com.moneytransfer.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.moneytransfer.exception.ExceptionWrapper;

/**
 * This call will be used to create a new connection for each trancations.
 * @author Vikas Gupta
 *
 */
public class DatabaseConnection {

	private static DatabaseConnection instance;
	private Connection connection;
	private String url = "jdbc:h2:~/moneytransfer";
	private String username = "vikas";
	private String password = "vikas";

	/**
	 * This method will be used to create database connection. 
	 * @return
	 * @throws ExceptionWrapper
	 */
	public Connection getConnection() throws ExceptionWrapper {
		try {
			Class.forName("org.h2.Driver");
			this.connection = DriverManager.getConnection(url, username, password);
		} catch (SQLException | ClassNotFoundException ex) {
			throw new ExceptionWrapper("Database Connection Creation Failed : " + ex.getMessage());
		}
		return connection;
	}

	public static DatabaseConnection getInstance() {
		if (instance == null) {
			instance = new DatabaseConnection();
		}
		return instance;
	}
}