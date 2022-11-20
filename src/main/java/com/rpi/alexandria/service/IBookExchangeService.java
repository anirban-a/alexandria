package com.rpi.alexandria.service;

import com.rpi.alexandria.model.Exchange;

import java.util.List;

public interface IBookExchangeService {

	void createExchange(Exchange exchange);

	void deleteExchange(Exchange exchange);

	List<Exchange> getAllExchangesByUserId(String userId);

	void markCompleted(String id, String userId);

}
