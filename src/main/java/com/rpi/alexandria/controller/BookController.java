package com.rpi.alexandria.controller;

import com.azure.core.annotation.QueryParam;
import com.rpi.alexandria.controller.response.AppResponse;
import com.rpi.alexandria.dto.BookDTO;
import com.rpi.alexandria.model.Book;
import com.rpi.alexandria.model.User;
import com.rpi.alexandria.service.BookService;
import com.rpi.alexandria.service.UserService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/book")
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class BookController extends BaseController {

	BookService bookService;

	public BookController(UserService userService, BookService bookService) {
		super(userService);
		this.bookService = bookService;
	}

	@PostMapping
	public ResponseEntity<AppResponse> addBook(@RequestBody BookDTO bookDTO) {
		User user = getUser();
		log.info("Received request to add book for user: {}", user.getUsername());
		bookService.addBook(user, bookDTO.getMappedEntity());
		AppResponse appResponse = buildAppResponse("Book added successfully", HttpStatus.OK);
		return new ResponseEntity<>(appResponse, appResponse.getHttpStatus());
	}

	@GetMapping
	public ResponseEntity<AppResponse> getBook(@QueryParam("isbn") String isbn) {
		log.info("Received request to fetch book by ISBN: {}", isbn);
		List<Book> books = bookService.findByISBN(isbn);
		List<BookDTO> bookDTOList = books.stream().map(BookDTO::of).collect(Collectors.toList());
		AppResponse<List<BookDTO>> appResponse = new AppResponse<>("Success", OffsetDateTime.now(), HttpStatus.OK, "",
				bookDTOList);
		return new ResponseEntity<>(appResponse, appResponse.getHttpStatus());
	}

}
