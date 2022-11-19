package com.rpi.alexandria.service;

import com.rpi.alexandria.model.Exchange;
import com.rpi.alexandria.repository.BookExchangeRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class BookExchangeService implements IBookExchangeService {
    BookExchangeRepository bookExchangeRepository;

    @Override
    public void createExchange(Exchange exchange) {
        String exchangeId1 = RandomStringUtils.random(5, true, true) + exchange.hashCode();
        String exchangeId2 = RandomStringUtils.random(5, true, true) + exchangeId1.hashCode();
        exchange.setId(exchangeId1);
        Exchange otherPartyExchange = new Exchange();
        otherPartyExchange.setId(exchangeId2);
        otherPartyExchange.setFirstPartyId(exchange.getOtherPartyId());
        otherPartyExchange.setOtherPartyId(exchange.getFirstPartyId());
        otherPartyExchange.setFirstPartyBookId(exchange.getOtherPartyBookId());
        otherPartyExchange.setOtherPartyBookId(exchange.getFirstPartyBookId());

        bookExchangeRepository.saveAll(List.of(exchange, otherPartyExchange));
    }

    @Override
    public void deleteExchange(Exchange exchange) {
        bookExchangeRepository.deleteById(exchange.getId());
    }
}
