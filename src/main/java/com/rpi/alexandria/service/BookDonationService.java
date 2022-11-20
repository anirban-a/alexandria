package com.rpi.alexandria.service;

import com.azure.cosmos.models.PartitionKey;
import com.rpi.alexandria.exception.ApplicationException;
import com.rpi.alexandria.model.Donation;
import com.rpi.alexandria.repository.BookDonationRepository;
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
public class BookDonationService implements IBookDonationService {

	BookDonationRepository bookDonationRepository;

	@Override
	public void createTransaction(Donation transaction) {
		transaction.computeId();
		bookDonationRepository.save(transaction);
	}

	@Override
	public void deleteTransaction(Donation transaction) {
		bookDonationRepository.deleteById(transaction.getId(), new PartitionKey(transaction.getFirstPartyId()));
	}

	@Override
	public List<Donation> getAllTransactionsByUserId(String userId) {
		return bookDonationRepository.findAll(new PartitionKey(userId));
	}

	@Override
	public void markCompleted(String id, String userId) {
		Donation donation = bookDonationRepository.findById(id, new PartitionKey(userId))
				.orElseThrow(() -> new ApplicationException(String.format("No such donation by id %s found", id)));
		donation.setCompleted(true);
		bookDonationRepository.save(donation);
	}

}
