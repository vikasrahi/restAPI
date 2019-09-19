package com.moneytransfer.handler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moneytransfer.exception.ExceptionWrapper;
import com.moneytransfer.exception.MoneyTransferException;
import com.moneytransfer.service.AccountService;
import com.moneytransfer.wrapper.Account;

public class AccountHandlerTest {

	private ObjectMapper objectMapperMock;
	private AccountService accountService;
	private Account account;
	private String requestBody;
	private AccountHandler accountHandler;

	@BeforeEach
	public void beforeTest() {
		account = new Account();
		account.setAccountNumber(1124);
		account.setAmount(100);
		account.setName("vikas");
		objectMapperMock = mock(ObjectMapper.class);
		accountService = mock(AccountService.class);
		requestBody = "{\n" + "	\"name\" : \"Vivek Tiwari\",\n" + "	\"amount\": 4000.90\n" + "}";
		accountHandler = new AccountHandler(accountService, objectMapperMock);

	}

	@Test
	void handlePOSTtest()
			throws ExceptionWrapper, MoneyTransferException, JsonParseException, JsonMappingException, IOException {
		Map<String, String[]> requestParams = new HashMap<>();
		String[] params = { "value1", "value2" };
		requestParams.put("key1", params);

		when(accountService.addAccount(any())).thenReturn(1234);
		when(objectMapperMock.readValue(anyString(), eq(Account.class))).thenReturn(account);

		Assertions.assertNotNull(accountHandler.handle(requestParams, requestBody, "POST"));
	}

	@Test
	void handleGETtest() throws ExceptionWrapper, MoneyTransferException {
		Map<String, String[]> requestParams = new HashMap<>();
		String[] params = { "7914" };
		requestParams.put("accountNumber", params);

		when(accountService.fetchAccount(any(), anyInt())).thenReturn(account);
		Assertions.assertNotNull(accountHandler.handle(requestParams, requestBody, "GET"));
	}

	@Test
	void handleGETMoneyTransferExceptiontest() throws MoneyTransferException, ExceptionWrapper {
		Map<String, String[]> requestParams = new HashMap<>();
		String[] params = { "7912" };
		requestParams.put("accountNumber", params);

		Assertions.assertThrows(MoneyTransferException.class, () -> {
			accountHandler.handle(requestParams, requestBody, "GET");
		});
	}
	
	@Test
	void handlePOSTMoneyTransferExceptiontest() throws MoneyTransferException, ExceptionWrapper {
		Map<String, String[]> requestParams = new HashMap<>();
		String[] params = { "7912" };
		requestParams.put("accountNumber", params);

		Assertions.assertThrows(MoneyTransferException.class, () -> {
			accountHandler.handle(requestParams, requestBody, "POST");
		});
	}
}
