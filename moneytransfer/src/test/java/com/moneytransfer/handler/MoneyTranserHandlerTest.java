package com.moneytransfer.handler;

import static org.mockito.ArgumentMatchers.any;
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
import com.moneytransfer.service.MoneyTransferService;
import com.moneytransfer.wrapper.MoneyTransfer;

public class MoneyTranserHandlerTest {
	
	private MoneyTranserHandler moneyTranserHandler;
	private MoneyTransferService moneyTransferServiceMock;
	private ObjectMapper objectMapperMock;
	private String requestBody;
	private MoneyTransfer moneyTransfer;
	
	@BeforeEach
	public void beforeTest() {
		objectMapperMock = mock(ObjectMapper.class);
		moneyTransferServiceMock = mock(MoneyTransferService.class);
		requestBody = "{\n" + 
				"	\"accountNumberFrom\" : \"7914\",\n" + 
				"	\"accountNumberTo\" : \"2324\",\n" + 
				"	\"amount\": 100\n" + 
				"}";
		moneyTransfer = new MoneyTransfer();
		moneyTransfer.setAccountNumberFrom(1234);
		moneyTransfer.setAccountNumberTo(5678);
		moneyTransfer.setAmount(100);
		
		moneyTranserHandler = new MoneyTranserHandler(moneyTransferServiceMock,objectMapperMock);
	}

	@Test
	void testHandlePUT() throws JsonParseException, JsonMappingException, IOException, ExceptionWrapper, MoneyTransferException {
		
		Map<String, String[]> requestParams = new HashMap<>();
		String[] params = { "7914" };
		requestParams.put("accountNumber", params);
		
		when(objectMapperMock.readValue(anyString(), eq(MoneyTransfer.class))).thenReturn(moneyTransfer);
		when(moneyTransferServiceMock.transferAmount(any())).thenReturn(Boolean.TRUE);
		Assertions.assertNotNull(moneyTranserHandler.handle(requestParams, requestBody, "PUT"));
	}
	
	@Test
	void testHandleExceptionWrapperPUT() throws JsonParseException, JsonMappingException, IOException, ExceptionWrapper, MoneyTransferException {
		
		Map<String, String[]> requestParams = new HashMap<>();
		String[] params = { "7914" };
		requestParams.put("accountNumber", params);
		moneyTransfer.setAmount(-1);
		when(objectMapperMock.readValue(anyString(), eq(MoneyTransfer.class))).thenReturn(moneyTransfer);
		when(moneyTransferServiceMock.transferAmount(any())).thenReturn(Boolean.TRUE);
		//Assertions.assertEquals(ExceptionWrapper.class, moneyTranserHandler.handle(requestParams, requestBody, "PUT"));
		
		Assertions.assertThrows(ExceptionWrapper.class, () -> {
			moneyTranserHandler.handle(requestParams, requestBody, "PUT");
		});
	}

}
