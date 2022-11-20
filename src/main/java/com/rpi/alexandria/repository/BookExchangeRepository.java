package com.rpi.alexandria.repository;

import com.azure.cosmos.models.PartitionKey;
import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.rpi.alexandria.model.Exchange;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookExchangeRepository extends CosmosRepository<Exchange, String> {

	List<Exchange> findAll(PartitionKey partitionKey);

}
