package com.rpi.alexandria.repository;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.rpi.alexandria.model.Book;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends CosmosRepository<Book, String> {
}
