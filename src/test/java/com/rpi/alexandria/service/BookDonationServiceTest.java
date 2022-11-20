package com.rpi.alexandria.service;

import com.azure.cosmos.models.PartitionKey;
import com.rpi.alexandria.model.Donation;
import com.rpi.alexandria.model.Exchange;
import com.rpi.alexandria.repository.BookDonationRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

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
	ArgumentCaptor<List<Exchange>> exchangeArgumentCaptor;

	@Captor
	ArgumentCaptor<String> exchangeIdArgumentCaptor;

	@Captor
	ArgumentCaptor<PartitionKey> partitionKeyArgumentCaptor;

	@AfterEach
	public void setup() {
		Mockito.reset(bookDonationRepository);
	}

	@Test
	public void contextLoads(){
		assertNotNull(bookDonationService);
	}
}
