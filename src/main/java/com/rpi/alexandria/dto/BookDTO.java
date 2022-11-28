package com.rpi.alexandria.dto;

import com.rpi.alexandria.model.Book;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;

@Data
@ToString
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookDTO implements Mappable<Book> {

	String id;

	@NotBlank(message = "ISBN is mandatory")
	String isbn;

	@NotBlank(message = "Book name is mandatory")
	String name;

	@NotBlank(message = "Book condition is mandatory")
	String condition;

	String description;

	Boolean forExchange;

	Boolean forGiveAway;

	String owner;

	Integer status = 0;

	public static BookDTO of(Book book) {
		return new BookDTO(book.getId(), book.getIsbn(), book.getName(), book.getCondition(), book.getDescription(),
				book.getForExchange(), book.getForGiveAway(), book.getListedBy().getUsername(), book.getStatus());
	}

	@Override
	public Book mappedEntity() {
		Book book = new Book();
		book.setIsbn(isbn);
		book.setName(name);
		book.setCondition(condition);
		book.setDescription(description);
		book.setForExchange(forExchange);
		book.setForGiveAway(forGiveAway);
		return book;
	}

}
