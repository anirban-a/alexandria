package com.rpi.alexandria.service;

import com.rpi.alexandria.model.Donation;

public interface IBookDonationService {
    void createDonation(Donation donationBook);
    void deleteDonation(Donation donationBook);
    void markDonationComplete(Donation donationBook);
}
