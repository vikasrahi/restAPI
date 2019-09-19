package com.moneytransfer.handler;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moneytransfer.exception.ExceptionWrapper;
import com.moneytransfer.exception.MoneyTransferException;
import com.moneytransfer.service.AccountService;
import com.moneytransfer.service.MoneyTransferService;
import com.moneytransfer.wrapper.MoneyTransfer;

/**
 * This Class use for handle all the request come from path '/transfer' with
 * request Method PUT.
 * 
 * @author Vikas Gupta
 *
 */
public class MoneyTranserHandler implements IHandler {

	private static final String PUT_METHOD = "PUT";
	private static final String PATH = "/transfer";
	private MoneyTransferService moneyTransferService = new MoneyTransferService();
	private ObjectMapper objectMapper = new ObjectMapper();
	private boolean transferSuccess = false;

	@Override
	public String handle(Object handle, String requestBody, String methodType)
			throws MoneyTransferException, ExceptionWrapper {
		if (PUT_METHOD.equals(methodType)) {
			try {
				if (requestBody != null && !requestBody.isEmpty()) {
					MoneyTransfer moneyTransfer = objectMapper.readValue(requestBody, MoneyTransfer.class);
					if (moneyTransfer.getAmount() < 0)
						throw new ExceptionWrapper(
								"Money Transfer is only allow with positive value of 'amount' field.");
					transferSuccess = moneyTransferService.transferAmount(moneyTransfer);
					if (transferSuccess) {
						return moneyTransfer.getAmount() + " has been transferred to : "
								+ moneyTransfer.getAccountNumberTo();
					}
				}
			} catch (IOException ex) {
				throw new MoneyTransferException("Exception during JSON parsing with reason : ", ex);
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
		return (PUT_METHOD.equals(methodType));
	}

	public MoneyTranserHandler() {

	}

	public MoneyTranserHandler(MoneyTransferService moneyTransferService, ObjectMapper objectMapper) {
		this.moneyTransferService = moneyTransferService;
		this.objectMapper = objectMapper;
	}
}
