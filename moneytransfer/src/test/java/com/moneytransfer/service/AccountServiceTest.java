package com.moneytransfer.service;

import static org.mockito.Mockito.mock;

import java.sql.Connection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.moneytransfer.exception.ExceptionWrapper;
import com.moneytransfer.repository.AccountRepository;
import com.moneytransfer.wrapper.Account;

public class AccountServiceTest {

	private AccountRepository accountRepository;
	private Account account;
	private Account acctResponse;
	private String message ="test";
	private Connection conn;
	
	private AccountService accountService;
	
	@BeforeEach
	  public void setup() {
		accountRepository = mock(AccountRepository.class);
		account = mock(Account.class);
		conn = mock(Connection.class);
		acctResponse = new Account();
		acctResponse.setAccountNumber(1234);
		acctResponse.setAmount(100);
		acctResponse.setName("vikas");
		
		accountService = new AccountService(accountRepository);
	}
	
	@Test
	public void addAccountSuccessTest() throws ExceptionWrapper {
		Mockito
        .when(accountRepository
            .addAccount(Mockito.any()))
        .thenReturn(1234);
		Assertions.assertEquals(1234,accountService.addAccount(account));
	}
	
	@Test
	public void addAccountFailureTest() throws ExceptionWrapper {
		Mockito
        .when(accountRepository
            .addAccount(Mockito.any())).thenThrow(new ExceptionWrapper(message));
		
		Assertions.assertThrows(ExceptionWrapper.class, () -> {
			accountService.addAccount(account);
		    });
	}
	@Test
	public void fetchAccountSuccessTest() throws ExceptionWrapper {
		Mockito
        .when(accountRepository
            .fetchAccount(Mockito.any(),Mockito.anyInt()))
        .thenReturn(acctResponse);
		Assertions.assertNotNull(accountService.fetchAccount(conn,1234));
	}
	
	@Test
	public void fetchAccountFailureTest() throws ExceptionWrapper {
		Mockito
        .when(accountRepository
            .fetchAccount(Mockito.any(),Mockito.anyInt())).thenThrow(new ExceptionWrapper(message));
		Assertions.assertThrows(ExceptionWrapper.class, () -> {
			accountService.addAccount(account);
		    });
	}
	

//	@Test
//	public void testWithdrawAmount() throws ExceptionWrapper {
//		Assertions.assertThrows(ExceptionWrapper.class, () -> {
//			accountService.withdrawAmount(conn, account, 10.0);
//		    });
//	}
}
