package com.rpi.alexandria.service;

import com.azure.cosmos.models.PartitionKey;
import com.rpi.alexandria.model.Donation;
import com.rpi.alexandria.repository.BookDonationRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@FieldDefaults(level = AccessLevel.PRIVATE)
@RunWith(MockitoJUnitRunner.class)
public class BookDonationServiceTest {

	@Mock
	final BookDonationRepository bookDonationRepository = Mockito.mock(BookDonationRepository.class);

	@InjectMocks
	ITransactableBookService<Donation> bookDonationService = new BookDonationService(bookDonationRepository);

	@Captor
	ArgumentCaptor<Donation> donationArgumentCaptor;

	@Captor
	ArgumentCaptor<String> donationIdArgumentCaptor;

	@Captor
	ArgumentCaptor<PartitionKey> partitionKeyArgumentCaptor;

	@AfterEach
	public void setup() {
		Mockito.reset(bookDonationRepository);
	}

	// test creation of book donation transaction.
	@Test
	public void test001() {
		Donation donation = new Donation();
		String firstPartyId = String.format("%s@test.com", RandomStringUtils.random(5));
		String otherPartyId = String.format("%s@test.com", RandomStringUtils.random(5));
		donation.setFirstPartyBookId(RandomStringUtils.random(10));
		donation.setFirstPartyId(firstPartyId);
		donation.setOtherPartyId(otherPartyId);

		Mockito.when(bookDonationRepository.save(Mockito.any())).thenReturn(null);

		bookDonationService.createTransaction(donation);

		Mockito.verify(bookDonationRepository, Mockito.times(1)).save(donationArgumentCaptor.capture());

		Donation capturedDonation = donationArgumentCaptor.getValue();

		assertEquals(donation.getId(), capturedDonation.getId());
		assertFalse(capturedDonation.getCompleted());
	}

	@Test
	public void test002() {
		Donation donation = new Donation();
		String firstPartyId = String.format("%s@test.com", RandomStringUtils.random(5));
		String otherPartyId = String.format("%s@test.com", RandomStringUtils.random(5));
		donation.setFirstPartyBookId(RandomStringUtils.random(10));
		donation.setFirstPartyId(firstPartyId);
		donation.setOtherPartyId(otherPartyId);

		Mockito.when(bookDonationRepository.findById(Mockito.any(), Mockito.any(PartitionKey.class)))
				.thenReturn(Optional.of(donation));

		bookDonationService.markCompleted(donation.getId(), donation.getFirstPartyId());

		Mockito.verify(bookDonationRepository, Mockito.times(1)).save(donationArgumentCaptor.capture());

		Donation capturedDonation = donationArgumentCaptor.getValue();

		assertEquals(donation.getId(), capturedDonation.getId());
		assertTrue(capturedDonation.getCompleted());
	}

}
