package com.rpi.alexandria.service;

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
    public void createTransaction(Donation book) {

    }

    @Override
    public void deleteTransaction(Donation book) {

    }

    @Override
    public List<Donation> getAllTransactionsByUserId(String userId) {
        return null;
    }

    @Override
    public void markCompleted(String id, String userId) {

    }
}
