package com.rpi.alexandria.controller;

import com.rpi.alexandria.controller.response.AppResponse;
import com.rpi.alexandria.model.Book;
import com.rpi.alexandria.model.User;
import com.rpi.alexandria.service.BookService;
import com.rpi.alexandria.service.UserService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	public ResponseEntity<AppResponse> addBook(@RequestBody Book book) {
		User user = getUser();
		log.info("Received request to add book for user: {}", user.getUsername());
		bookService.addBook(user, book);
		AppResponse appResponse = buildAppResponse("Book added successfully", HttpStatus.OK);
		return new ResponseEntity<>(appResponse, appResponse.getHttpStatus());
	}

}
