package com.rpi.alexandria.repository;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.rpi.alexandria.model.Donation;
import org.springframework.stereotype.Repository;

@Repository
public interface BookDonationRepository extends CosmosRepository<Donation, String> {
}
