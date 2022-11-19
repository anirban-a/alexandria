package com.rpi.alexandria.service;

import com.azure.cosmos.models.PartitionKey;
import com.rpi.alexandria.exception.ApplicationException;
import com.rpi.alexandria.model.Exchange;
import com.rpi.alexandria.repository.BookExchangeRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
@Slf4j
public class BookExchangeService implements IBookExchangeService {
    BookExchangeRepository bookExchangeRepository;

    @Override
    public void createExchange(Exchange exchange) {
        exchange.computeId();
        Exchange otherPartyExchange = exchange.getOtherPartyExchange();
        bookExchangeRepository.saveAll(List.of(exchange, otherPartyExchange));
    }

    @Override
    public void deleteExchange(Exchange exchange) {
        bookExchangeRepository.deleteById(exchange.getId());
    }

    @Override
    public List<Exchange> getAllExchangesByUserId(String userId) {
        return bookExchangeRepository.findAll(new PartitionKey(userId));
    }

    @Override
    public void markCompleted(String id, String userId) {
        Exchange exchange = bookExchangeRepository
                .findById(id, new PartitionKey(userId)).orElseThrow(() -> new ApplicationException(String.format("No such exchange by id %s found", id)));
        exchange.setCompleted(true);
        String otherPartyExchangeId = exchange.getId().split("_")[0]+"_"+exchange.getOtherPartyId();
        log.info(otherPartyExchangeId);
        Exchange otherPartyExchange = bookExchangeRepository
                .findById(otherPartyExchangeId, new PartitionKey(exchange.getOtherPartyId())).orElseThrow(() -> new ApplicationException(String.format("No such exchange by id %s found for other party", id)));
        otherPartyExchange.setCompleted(true);
        bookExchangeRepository.saveAll(List.of(exchange, otherPartyExchange));
    }
}
