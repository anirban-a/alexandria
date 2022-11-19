package com.rpi.alexandria.service;

import com.rpi.alexandria.model.Exchange;

public interface IBookExchangeService {
    void createExchange(Exchange exchange);
    void deleteExchange(Exchange exchange);
}
