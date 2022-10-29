package com.rpi.alexandria.repository;

import com.rpi.alexandria.model.Book;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends ElasticsearchRepository<Book, String> {

	List<Book> findByIsbn(String isbn);

	List<Book> findAllByListedByUsername(String username);

}
