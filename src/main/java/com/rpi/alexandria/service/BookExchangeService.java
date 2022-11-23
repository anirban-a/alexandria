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
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
@Slf4j
public class BookExchangeService implements IBookExchangeService {

	BookExchangeRepository bookExchangeRepository;

	@Override
	public void createTransaction(Exchange transaction) {
		transaction.computeId();
		Exchange otherPartyExchange = transaction.deriveOtherPartyExchange();
		bookExchangeRepository.saveAll(List.of(transaction, otherPartyExchange));
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

		deleteTransaction(exchange);
	}

}
