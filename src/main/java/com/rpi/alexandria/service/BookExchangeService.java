package com.rpi.alexandria.service;

import com.rpi.alexandria.model.Exchange;
import com.rpi.alexandria.repository.BookExchangeRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookExchangeService implements IBookExchangeService{
    BookExchangeRepository bookExchangeRepository;
    @Override
    public void createExchange(Exchange exchange) {

    }

    @Override
    public void deleteExchange(Exchange exchange) {

    }
}
