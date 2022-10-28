package com.rpi.alexandria.repository;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.rpi.alexandria.model.Book;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends ElasticsearchRepository<Book, String> {

}
