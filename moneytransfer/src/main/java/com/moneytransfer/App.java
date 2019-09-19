package com.moneytransfer;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.moneytransfer.controller.MoneyTransferController;
import com.moneytransfer.exception.ExceptionWrapper;
import com.moneytransfer.repository.DatabaseConnection;

/**
 * Application created for Money Transfer between accounts
 *
 */
public class App {
	static final String JDBC_DRIVER = "org.h2.Driver";
	static final String DB_URL = "jdbc:h2:~/moneytransfer";

	// Database credentials
	static final String USER = "vikas";
	static final String PASS = "vikas";

	public static void main(String[] args) {

		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
		context.setContextPath("/");

		Server jettyServer = new Server(9998);
		jettyServer.setHandler(context);

		ServletHolder servletHolder = context.addServlet(MoneyTransferController.class.getName(), "/*");
		servletHolder.setInitOrder(1);

		createTable();// As one time process of table creation has been done from here, which can be
						// done as SQL create script as part of DB script execution.

		try {
			jettyServer.start();
			jettyServer.join();
		} catch (Exception e) {
			System.out.println("Exception :: " + e.getCause());
		} finally {
			jettyServer.destroy();
		}

	}

	/**
	 * This method will be used to create Account table.
	 */
	private static void createTable() {
		Connection conn = null;
		Statement stmt = null;
		try {
			conn = DatabaseConnection.getInstance().getConnection();
			stmt = conn.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS ACCOUNT " + "(id INTEGER not NULL, " + " name VARCHAR(255), "
					+ " amount DOUBLE, " + " PRIMARY KEY ( id ))";
			stmt.executeUpdate(sql);

			// STEP 4: Clean-up environment
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			System.out.println("SQLException :: " + e.getMessage());
		} catch (ExceptionWrapper mEx) {
			System.out.println("ExceptionWrapper :: " + mEx.getMessage());
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException se2) {
			} // nothing we can do
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se2) {
			} // nothing we can do
		} // end try

	}

}
