package com.rpi.alexandria.service;

import com.rpi.alexandria.exception.ApplicationException;
import com.rpi.alexandria.model.Book;
import com.rpi.alexandria.model.User;
import com.rpi.alexandria.repository.BookRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookService {

	BookRepository bookRepository;

	ElasticsearchOperations elasticsearchOperations;

	/**
	 * @param user
	 * @param book
	 * @implNote This API will add a book to a central book index.
	 */
	public void addBook(User user, Book book) {
		book.setId(computeBookId(user, book));
		book.setListedBy(user);
		bookRepository.save(book);
	}

	public List<Book> findByISBN(String isbn) {
		return bookRepository.findByIsbn(isbn);
	}

	public Book findById(String id) {
		return bookRepository.findById(id)
				.orElseThrow(() -> new ApplicationException(String.format("No such book by id: %s found", id)));
	}

	public void setBookStatus(String id, int bookStatus) {
		Book book = findById(id);
		book.setStatus(bookStatus);
		bookRepository.save(book);
	}

	public void deleteById(String id) {
		bookRepository.deleteById(id);
	}

	public List<Book> findAllBooksByName(String name) {
		MatchPhraseQueryBuilder queryBuilder = QueryBuilders.matchPhraseQuery("name", name);
		Query searchQuery = new NativeSearchQueryBuilder().withQuery(queryBuilder).build();
		SearchHits<Book> bookSearchHit = elasticsearchOperations.search(searchQuery, Book.class,
				IndexCoordinates.of("book"));
		return bookSearchHit.stream().map(SearchHit::getContent).collect(Collectors.toList());
	}

	public List<Book> findAllBooksForUser(String username) {
		return bookRepository.findAllByListedByUsername(username);
	}

	private String computeBookId(User user, Book book) {
		return String.format("%s_%s", user.hashCode(), book.getIsbn());
	}

}
