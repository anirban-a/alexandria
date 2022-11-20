package com.rpi.alexandria.repository;

import com.azure.cosmos.models.PartitionKey;
import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.rpi.alexandria.model.Donation;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookDonationRepository extends CosmosRepository<Donation, String> {
    List<Donation> findAll(PartitionKey partitionKey);
}
