package com.rpi.alexandria.dto;

import com.rpi.alexandria.model.Book;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Data
@ToString
@AllArgsConstructor
public class BookDTO implements Mappable<Book> {

	@NotBlank(message = "ISBN is mandatory")
	String isbn;

	@NotBlank(message = "Book name is mandatory")
	String name;

	@NotBlank(message = "Book condition is mandatory")
	String condition;

	String description;

	Boolean forExchange;

	Boolean forGiveAway;

	public static BookDTO of(Book book) {
		return new BookDTO(book.getIsbn(), book.getName(), book.getCondition(), book.getDescription(),
				book.getForExchange(), book.getForGiveAway());
	}

	@Override
	public Book getMappedEntity() {
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
