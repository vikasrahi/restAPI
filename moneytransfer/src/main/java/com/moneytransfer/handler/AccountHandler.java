package com.moneytransfer.handler;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moneytransfer.exception.ExceptionWrapper;
import com.moneytransfer.exception.MoneyTransferException;
import com.moneytransfer.service.AccountService;
import com.moneytransfer.wrapper.Account;

/**
 * This Class use for handle all the request come from path '/account' with request Method (GET/POST/PUT).
 * @author Vikas Gupta
 *
 */
public class AccountHandler implements IHandler {

	private static final String POST_METHOD = "POST";
	private static final String GET_METHOD = "GET";
	private static final String PUT_METHOD = "PUT";
	private static final String PATH = "/account";
	private static final String ACCOUNT_PARAM = "accountNumber";

	private ObjectMapper objectMapper = new ObjectMapper();
	private AccountService accountService = new AccountService();
	
	@Override
	public String handle(Object handle, String requestBody, String methodType)
			throws MoneyTransferException, ExceptionWrapper {
		Map<String, String[]> requestParams = (Map<String, String[]>) handle;
		if (POST_METHOD.equals(methodType)) {
			try {
				if (requestBody != null && !requestBody.isEmpty()) {
					Account account = objectMapper.readValue(requestBody, Account.class);
					if(account.getAmount() < 0) throw new ExceptionWrapper("Account creation is only allow with positive value of 'amount' field");
					int accountNumber = accountService.addAccount(account);
					return "Account has been Created with Account Number : " + accountNumber;
				}
			} catch (IOException ex) {
				throw new MoneyTransferException("Exception during JSON parsing with reason : ", ex);
			}

		} else if (GET_METHOD.equals(methodType)) {
			for (Map.Entry<String, String[]> entry : requestParams.entrySet()) {
				try {
					if (ACCOUNT_PARAM.equals(entry.getKey())) {
						return objectMapper.writeValueAsString(accountService.fetchAccount(null,Integer.parseInt(entry.getValue()[0])));
					}
				} catch (IOException ex) {
					throw new MoneyTransferException("Exception during JSON parsing with reason : ", ex);
				}
			}
		}
		return "";
	}

	@Override
	public boolean supportPath(String path) {
		return PATH.equals(path);
	}

	@Override
	public boolean supportMethod(String methodType) {
		return (POST_METHOD.equals(methodType) || GET_METHOD.equals(methodType) || PUT_METHOD.equals(methodType));
	}
	
	public AccountHandler() {
		
	}
	public AccountHandler(AccountService accountService, ObjectMapper objectMapper) {
		this.accountService = accountService;
		this.objectMapper = objectMapper;
	}

}
