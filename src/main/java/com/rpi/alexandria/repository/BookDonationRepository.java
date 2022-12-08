package com.rpi.alexandria.repository;

import com.azure.cosmos.models.PartitionKey;
import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.rpi.alexandria.model.Donation;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface BookDonationRepository extends CosmosRepository<Donation, String> {

  List<Donation> findAll(PartitionKey partitionKey);

}
