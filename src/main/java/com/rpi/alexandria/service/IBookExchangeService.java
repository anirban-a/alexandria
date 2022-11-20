package com.rpi.alexandria.service;

import com.rpi.alexandria.model.Exchange;

import java.util.List;

public interface IBookExchangeService extends ITransactableBookService<Exchange>{

	void createTransaction(Exchange exchange);

	void deleteTransaction(Exchange exchange);

	List<Exchange> getAllTransactionsByUserId(String userId);

	void markCompleted(String id, String userId);

}
