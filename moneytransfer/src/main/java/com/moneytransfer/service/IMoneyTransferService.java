package com.moneytransfer.service;

import com.moneytransfer.wrapper.MoneyTransfer;

/**
 * This interface define a method for transfer amount between accounts.
 * @author Vikas Gupta
 *
 */
public interface IMoneyTransferService {
	
    boolean transferAmount(MoneyTransfer moneyTransfer) throws Exception;
}
