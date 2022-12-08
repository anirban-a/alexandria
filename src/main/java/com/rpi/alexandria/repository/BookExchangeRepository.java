package com.rpi.alexandria.repository;

import com.azure.cosmos.models.PartitionKey;
import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.rpi.alexandria.model.Exchange;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface BookExchangeRepository extends CosmosRepository<Exchange, String> {

  List<Exchange> findAll(PartitionKey partitionKey);

}
