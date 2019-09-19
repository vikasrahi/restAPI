package com.moneytransfer;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.moneytransfer.wrapper.Account;
import com.moneytransfer.wrapper.MoneyTransfer;

/**
 * This class will be used as integration test for money transfer application.
 * @author Vikas Gupta
 *
 */
public class MoneyTransferIntegartionTestRestAssured extends TestBase {
	private final Gson gson = new Gson();

	/**
	 * This method will be used for test create account.
	 */
	@Test
	public void addAccountTestSuccess() {
		Account account = new Account();
		account.setName("Vikas");
		account.setAmount(2000);
		String requestBody = gson.toJson(account);

		RequestSpecification request = RestAssured.given();
		request.body(requestBody);
		Response response = request.post("/account");
		Assertions.assertNotNull(response.asString());
	}

	/**
	 * THis will be failure due to wrong account number
	 */
	@Test
	public void getAccountTestFailure() {
		RestAssured.given().when().get("/account?accountNumber=7912").then().assertThat().statusCode(404);
	}

	/**
	 * This method will be used for test fetch account after create one.
	 */
	@Test
	public void getAccountTestSuccess() {

		String accountNumber = createAccount("viaks",1000);
		Map<String, String> parametersMap = new HashMap<>();
		parametersMap.put("accountNumber", accountNumber);

		RestAssured.given().when().parameters(parametersMap).get("/account").then().assertThat().statusCode(200);
	}
	/**
	 * This method is use to test money transfer success scenario.
	 */
	@Test
	public void moneyTrnasferSucess() {
		
		String accountNumberFrom = createAccount("krishna",4000);
		String accountNumberTo = createAccount("ram",2000);
		double amountToBeTranfered = 500.0;
		MoneyTransfer moneyTransfer = new MoneyTransfer();
		moneyTransfer.setAccountNumberFrom(Integer.parseInt(accountNumberFrom));
		moneyTransfer.setAccountNumberTo(Integer.parseInt(accountNumberTo));
		moneyTransfer.setAmount(amountToBeTranfered);
		
		String requestBody = gson.toJson(moneyTransfer);
		
		RequestSpecification request = RestAssured.given();
		request.body(requestBody);
		Response response = request.put("/transfer");
		
		// This logic is required due to response has been set as mix of Message and return data value. - START
		StringBuilder sb = new StringBuilder();
		sb.append(amountToBeTranfered);
		sb.append(" has been transferred to : ");
		sb.append(accountNumberTo);
		// This logic is required due to response has been set as mix of Message and return data value. - END
		
		Assertions.assertEquals(sb.toString(), response.asString());
		
	}

	/**
	 * This Test will be test exception scenario when account does not have sufficient balance to transfer.
	 */
	@Test
	public void moneyTrnasferExcetionInsufficientBalanceOfFromAccount() {
		
		String accountNumberFrom = createAccount("krishna",4000);
		String accountNumberTo = createAccount("ram",2000);
		double amountToBeTranfered = 40000.0;
		MoneyTransfer moneyTransfer = new MoneyTransfer();
		moneyTransfer.setAccountNumberFrom(Integer.parseInt(accountNumberFrom));
		moneyTransfer.setAccountNumberTo(Integer.parseInt(accountNumberTo));
		moneyTransfer.setAmount(amountToBeTranfered);
		
		String requestBody = gson.toJson(moneyTransfer);
		
		RequestSpecification request = RestAssured.given();
		request.body(requestBody);
		Response response = request.put("/transfer");
		
		// This logic is required due to response has been set as mix of Message and return data value. - START
		StringBuilder sb = new StringBuilder();
		sb.append("Account Number - ");
		sb.append(accountNumberFrom);
		sb.append(" do not have sufficient amout to transfer.");
		// This logic is required due to response has been set as mix of Message and return data value. - END
		
		Assertions.assertEquals(sb.toString(), response.asString());
		
	}
	
	/**
	 * Trying to transfer amount by providing wrong account number.
	 */
	@Test
	public void moneyTrnasferExcetionAccountNumberWrong() {
		
		int accountNumberNotExist = 1820;
		String accountNumberTo = createAccount("ram",2000);
		double amountToBeTranfered = 40000.0;
		MoneyTransfer moneyTransfer = new MoneyTransfer();
		moneyTransfer.setAccountNumberFrom(accountNumberNotExist);
		moneyTransfer.setAccountNumberTo(Integer.parseInt(accountNumberTo));
		moneyTransfer.setAmount(amountToBeTranfered);
		
		String requestBody = gson.toJson(moneyTransfer);
		
		RequestSpecification request = RestAssured.given();
		request.body(requestBody);
		Response response = request.put("/transfer");
		
		// This logic is required due to response has been set as mix of Message and return data value. - START
		StringBuilder sb = new StringBuilder();
		sb.append("Account not found with Account Number : ");
		sb.append(accountNumberNotExist);
		// This logic is required due to response has been set as mix of Message and return data value. - END
		
		Assertions.assertEquals(sb.toString(), response.asString());
		
	}
	/**
	 * common method to create account and return account number which will be used for fetch.
	 * @param name
	 * @param amount
	 * @return
	 */
	private String createAccount(String name, double amount) {
		String accountNumber = "";
		Account account = new Account();
		account.setName(name);
		account.setAmount(amount);
		
		String requestBody = gson.toJson(account);

		RequestSpecification request = RestAssured.given();
		request.body(requestBody);
		Response response = request.post("/account");
		if (response.asString() != null && response.asString().contains(":")) {
			String responseArr[] = response.asString().split(":");
			accountNumber = responseArr[1].trim();
		}
		return accountNumber;
	}

}