package com.rpi.alexandria.repository;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.rpi.alexandria.model.Exchange;
import org.springframework.stereotype.Repository;

@Repository
public interface BookExchangeRepository extends CosmosRepository<Exchange, String> {
}
