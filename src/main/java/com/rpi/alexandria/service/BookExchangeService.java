package com.rpi.alexandria.service;

import com.azure.cosmos.models.PartitionKey;
import com.rpi.alexandria.exception.ApplicationException;
import com.rpi.alexandria.model.Exchange;
import com.rpi.alexandria.repository.BookExchangeRepository;
import com.rpi.alexandria.repository.BookRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
@Slf4j
public class BookExchangeService implements IBookExchangeService {

	BookExchangeRepository bookExchangeRepository;

	BookService bookService;

	@Override
	public void createTransaction(Exchange transaction) {
		transaction.computeId();
		Exchange otherPartyExchange = transaction.deriveOtherPartyExchange();
		List<Exchange> transactions = List.of(transaction, otherPartyExchange);
		transactions.stream().map(Exchange::getFirstPartyBookId).filter(ObjectUtils::isNotEmpty)
				.forEach(bookId -> bookService.setBookStatus(bookId, 1));
		bookExchangeRepository.saveAll(transactions);
	}

	@Override
	public void deleteTransaction(Exchange transaction) {
		String otherPartyExchangeId = transaction.deriveOtherPartyExchangeId();
		log.info("Deleting Ids: {}, {}", transaction.getId(), otherPartyExchangeId);
		bookExchangeRepository.deleteById(transaction.getId(), new PartitionKey(transaction.getFirstPartyId()));
		bookExchangeRepository.deleteById(otherPartyExchangeId, new PartitionKey(transaction.getOtherPartyId()));
	}

	@Override
	public List<Exchange> getAllTransactionsByUserId(String userId) {
		return bookExchangeRepository.findAll(new PartitionKey(userId));
	}

	@Override
	public void markCompleted(String id, String userId) {
		Exchange exchange = bookExchangeRepository.findById(id, new PartitionKey(userId))
				.orElseThrow(() -> new ApplicationException(String.format("No such exchange by id %s found", id)));
		exchange.setCompleted(true);
		String otherPartyExchangeId = exchange.getId().split("_")[0] + "_" + exchange.getOtherPartyId();
		log.info(otherPartyExchangeId);
		Exchange otherPartyExchange = bookExchangeRepository
				.findById(otherPartyExchangeId, new PartitionKey(exchange.getOtherPartyId()))
				.orElseThrow(() -> new ApplicationException(
						String.format("No such exchange by id %s found for other party", id)));
		otherPartyExchange.setCompleted(true);
		// bookExchangeRepository.saveAll(List.of(exchange, otherPartyExchange));
		List<Exchange> exchangeList = List.of(exchange, otherPartyExchange);
		exchangeList.stream().filter(ex -> Objects.nonNull(ex.getFirstPartyBookId())).map(Exchange::getFirstPartyBookId)
				.forEach(bookId -> bookService.setBookStatus(bookId, 2));
		// bookService.setBookStatus(id, 2);
		deleteTransaction(exchange);
	}

	public void rejectTransaction(String id, String userId) {
		Exchange exchange = bookExchangeRepository.findById(id, new PartitionKey(userId))
				.orElseThrow(() -> new ApplicationException(String.format("No such exchange by id %s found", id)));

		String otherPartyExchangeId = exchange.getId().split("_")[0] + "_" + exchange.getOtherPartyId();
		log.info(otherPartyExchangeId);
		Exchange otherPartyExchange = bookExchangeRepository
				.findById(otherPartyExchangeId, new PartitionKey(exchange.getOtherPartyId()))
				.orElseThrow(() -> new ApplicationException(
						String.format("No such exchange by id %s found for other party", id)));

		List<Exchange> exchangeList = List.of(exchange, otherPartyExchange);
		exchangeList.stream().filter(ex -> Objects.nonNull(ex.getFirstPartyBookId())).map(Exchange::getFirstPartyBookId)
				.forEach(bookId -> bookService.setBookStatus(bookId, 0));
		deleteTransaction(exchange);
		log.info("Reversed transaction.");
	}

}
