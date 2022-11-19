package com.rpi.alexandria.service;

import com.rpi.alexandria.model.Exchange;
import com.rpi.alexandria.repository.BookExchangeRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@FieldDefaults(level = AccessLevel.PRIVATE)
@RunWith(MockitoJUnitRunner.class)
public class BookExchangeServiceTest {
    final BookExchangeRepository bookExchangeRepository = Mockito.mock(BookExchangeRepository.class);
    IBookExchangeService bookExchangeService;
    @Captor
    ArgumentCaptor<List<Exchange>> exchangeArgumentCaptor;

    @BeforeAll
    void setup() {
        bookExchangeService = new BookExchangeService(bookExchangeRepository);
    }

    @BeforeEach
    void cleanUp() {
        Mockito.reset(bookExchangeRepository);
    }

    @Test
    public void test001() {
        Exchange exchange = new Exchange();
        String firstPartyId = String.format("%s@test.com", RandomStringUtils.random(5));
        String otherPartyId = String.format("%s@test.com", RandomStringUtils.random(5));
        exchange.setId(RandomStringUtils.random(10));
        exchange.setFirstPartyBookId(RandomStringUtils.random(10));
        exchange.setOtherPartyBookId(RandomStringUtils.random(10));
        exchange.setFirstPartyId(firstPartyId);
        exchange.setOtherPartyId(otherPartyId);

        Mockito.when(bookExchangeRepository.saveAll(Mockito.anyCollection())).thenReturn(null);
        Mockito.verify(bookExchangeRepository, Mockito.times(1)).saveAll(exchangeArgumentCaptor.capture());

        List<Exchange> exchangeList = exchangeArgumentCaptor.getValue();

        assertEquals(2, exchangeList.size());
        assertTrue(exchangeList.stream().map(Exchange::getFirstPartyId).anyMatch(id -> id.equals(exchange.getFirstPartyId())));
        assertTrue(exchangeList.stream().map(Exchange::getOtherPartyId).anyMatch(id -> id.equals(exchange.getFirstPartyId())));
    }
}
