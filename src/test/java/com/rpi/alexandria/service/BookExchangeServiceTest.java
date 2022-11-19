package com.rpi.alexandria.service;

import com.azure.cosmos.models.PartitionKey;
import com.rpi.alexandria.model.Exchange;
import com.rpi.alexandria.repository.BookExchangeRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@FieldDefaults(level = AccessLevel.PRIVATE)
@RunWith(MockitoJUnitRunner.class)
public class BookExchangeServiceTest {
    @Mock
    final BookExchangeRepository bookExchangeRepository = Mockito.mock(BookExchangeRepository.class);
    @InjectMocks
    IBookExchangeService bookExchangeService = new BookExchangeService(bookExchangeRepository);
    @Captor
    ArgumentCaptor<List<Exchange>> exchangeArgumentCaptor;

    @AfterEach
    public void setup() {
        Mockito.reset(bookExchangeRepository);
    }

    //    test creation of exchange
    @Test
    public void test001() {
        Exchange exchange = new Exchange();
        String firstPartyId = String.format("%s@test.com", RandomStringUtils.random(5));
        String otherPartyId = String.format("%s@test.com", RandomStringUtils.random(5));
        exchange.setFirstPartyBookId(RandomStringUtils.random(10));
        exchange.setOtherPartyBookId(RandomStringUtils.random(10));
        exchange.setFirstPartyId(firstPartyId);
        exchange.setOtherPartyId(otherPartyId);
//        exchange.computeId();

        Mockito.when(bookExchangeRepository.saveAll(Mockito.anyCollection())).thenReturn(null);

//        call the service
        bookExchangeService.createExchange(exchange);

        Mockito.verify(bookExchangeRepository, Mockito.times(1)).saveAll(exchangeArgumentCaptor.capture());

        List<Exchange> exchangeList = exchangeArgumentCaptor.getValue();

        assertEquals(2, exchangeList.size());

        assertEquals(2, exchangeList.stream().map(Exchange::getId).map(Optional::ofNullable).filter(Optional::isPresent).count());
        assertEquals(1, exchangeList.stream().map(Exchange::getId).filter(id -> id.contains(exchange.getFirstPartyId())).count());
        assertEquals(1, exchangeList.stream().map(Exchange::getId).filter(id -> id.contains(exchange.getOtherPartyId())).count());
        assertTrue(exchangeList.stream().map(Exchange::getFirstPartyId).anyMatch(id -> id.equals(exchange.getFirstPartyId())));
        assertTrue(exchangeList.stream().map(Exchange::getOtherPartyId).anyMatch(id -> id.equals(exchange.getFirstPartyId())));
        assertEquals(0, exchangeList.stream().filter(Exchange::getCompleted).count());
    }

    @Test
    public void test002() {
        Exchange exchange = new Exchange();
        String firstPartyId = String.format("%s@test.com", RandomStringUtils.random(5));
        String otherPartyId = String.format("%s@test.com", RandomStringUtils.random(5));
        exchange.setId(RandomStringUtils.random(10));
        exchange.setFirstPartyBookId(RandomStringUtils.random(10));
        exchange.setOtherPartyBookId(RandomStringUtils.random(10));
        exchange.setFirstPartyId(firstPartyId);
        exchange.setOtherPartyId(otherPartyId);

        Mockito.when(bookExchangeRepository.findAll(new PartitionKey(Mockito.any()))).thenReturn(List.of(exchange));

        bookExchangeService.getAllExchangesByUserId(exchange.getFirstPartyId());

        Mockito.verify(bookExchangeRepository, Mockito.times(1)).findAll(Mockito.any(PartitionKey.class));
    }

    //    test mark book exchange completed.
    @Test
    public void test003() {
        Exchange exchange = new Exchange();
        String firstPartyId = String.format("%s@test.com", RandomStringUtils.random(5, false, true));
        String otherPartyId = String.format("%s@test.com", RandomStringUtils.random(5, false, true));
        exchange.setFirstPartyBookId(RandomStringUtils.random(10));
        exchange.setOtherPartyBookId(RandomStringUtils.random(10));
        exchange.setFirstPartyId(firstPartyId);
        exchange.setOtherPartyId(otherPartyId);
        exchange.computeId();


        Exchange otherPartyExchange = exchange.getOtherPartyExchange();

        Mockito.when(bookExchangeRepository.findById(exchange.getId(), new PartitionKey(exchange.getFirstPartyId()))).thenReturn(Optional.of(exchange));
        Mockito.when(bookExchangeRepository.findById(otherPartyExchange.getId(), new PartitionKey(exchange.getOtherPartyId()))).thenReturn(Optional.of(otherPartyExchange));

        bookExchangeService.markCompleted(exchange.getId(), exchange.getFirstPartyId());

        Mockito.verify(bookExchangeRepository, Mockito.times(1)).saveAll(exchangeArgumentCaptor.capture());
        List<Exchange> exchangeList = exchangeArgumentCaptor.getValue();
        assertEquals(2, exchangeList.stream().filter(Exchange::getCompleted).count());
    }
}