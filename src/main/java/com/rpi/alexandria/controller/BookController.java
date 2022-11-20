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
import org.apache.commons.lang3.StringUtils;
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
@CrossOrigin("http://localhost:${ui.port}")
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
		bookService.addBook(user, bookDTO.mappedEntity());
		AppResponse appResponse = buildAppResponse("Book added successfully", HttpStatus.OK);
		return new ResponseEntity<>(appResponse, appResponse.getHttpStatus());
	}

	@GetMapping
	public ResponseEntity<AppResponse<List<BookDTO>>> getBookByISBN(@QueryParam("isbn") String isbn,
			@QueryParam("name") String name) {
		if (StringUtils.isEmpty(isbn) && StringUtils.isEmpty(name)) {
			AppResponse appResponse = new AppResponse("Both ISBN & book name is empty", OffsetDateTime.now(),
					HttpStatus.BAD_REQUEST, "", null);
			return new ResponseEntity<>(appResponse, appResponse.getHttpStatus());
		}

		// as of now, we will restrict to search by only name or ISBN. Both are not
		// allowed.
		if (!StringUtils.isEmpty(isbn) && !StringUtils.isEmpty(name)) {
			AppResponse appResponse = new AppResponse("too many parameters given", OffsetDateTime.now(),
					HttpStatus.BAD_REQUEST, "", null);
			return new ResponseEntity<>(appResponse, appResponse.getHttpStatus());
		}
		List<Book> books;
		if (!StringUtils.isEmpty(isbn)) {
			log.info("Received request to fetch book by ISBN: {}", isbn);
			books = bookService.findByISBN(isbn);
		}
		else {
			log.info("Received request to fetch book by name: {}", name);
			books = bookService.findAllBooksByName(name);
		}
		List<BookDTO> bookDTOList = books.stream().map(BookDTO::of).collect(Collectors.toList());
		AppResponse<List<BookDTO>> appResponse = new AppResponse<>("Success", OffsetDateTime.now(), HttpStatus.OK, "",
				bookDTOList);
		return new ResponseEntity<>(appResponse, appResponse.getHttpStatus());
	}

	@GetMapping("/collection")
	public ResponseEntity<AppResponse<List<BookDTO>>> getBooksForUser() {
		User user = getUser();
		List<BookDTO> bookDTOList = bookService.findAllBooksForUser(user.getUsername()).stream().map(BookDTO::of)
				.collect(Collectors.toList());
		AppResponse<List<BookDTO>> appResponse = new AppResponse<>("Success", OffsetDateTime.now(), HttpStatus.OK, "",
				bookDTOList);
		return new ResponseEntity<>(appResponse, appResponse.getHttpStatus());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<AppResponse> deleteBook(@PathVariable("id") String id) {
		log.info("Received delete book request");
		bookService.deleteById(id);
		AppResponse appResponse = buildAppResponse("Book deleted successfully", HttpStatus.OK);
		return new ResponseEntity<>(appResponse, appResponse.getHttpStatus());
	}

}
