package com.rpi.alexandria.service;

import com.rpi.alexandria.model.Book;
import com.rpi.alexandria.model.User;
import com.rpi.alexandria.repository.BookRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookService {

	BookRepository bookRepository;

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

	public List<Book> findAllBooksHavingNameLike(String name) {
		return bookRepository.findByNameContaining(name);
	}

	private String computeBookId(User user, Book book) {
		return String.format("%s_%s", user.hashCode(), book.getIsbn());
	}

}
