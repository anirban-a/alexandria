package com.rpi.alexandria.controller;

import com.rpi.alexandria.controller.response.AppResponse;
import com.rpi.alexandria.model.Exchange;
import com.rpi.alexandria.service.BookExchangeService;
import com.rpi.alexandria.service.UserService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/book/exchange")
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@CrossOrigin("http://localhost:${ui.port}")
public class BookExchangeController extends BaseController {

	BookExchangeService bookExchangeService;

	public BookExchangeController(UserService userService, BookExchangeService bookExchangeService) {
		super(userService);
		this.bookExchangeService = bookExchangeService;
	}

	@GetMapping
	public ResponseEntity<AppResponse> getAllExchange() {
		String username = getUser().getUsername();
		List<Exchange> exchangeList = bookExchangeService.getAllTransactionsByUserId(username);
		AppResponse<List<Exchange>> appResponse = buildAppResponse("", HttpStatus.OK);
		appResponse.setData(exchangeList);
		return new ResponseEntity<>(appResponse, appResponse.getHttpStatus());
	}

	@PostMapping
	public ResponseEntity<AppResponse> createExchange(@RequestBody Exchange exchange) {
		log.info("Received book exchange request");
		bookExchangeService.createTransaction(exchange);
		AppResponse appResponse = buildAppResponse("Book marked for exchange successfully", HttpStatus.OK);
		return new ResponseEntity<>(appResponse, appResponse.getHttpStatus());
	}

	@PostMapping("/complete/{id}")
	public ResponseEntity<AppResponse> markComplete(@PathVariable String id) {
		log.info("Received book exchange request");
		String username = getUser().getUsername();
		bookExchangeService.markCompleted(id, username);
		AppResponse appResponse = buildAppResponse("Book exchange marked complete successfully", HttpStatus.OK);
		return new ResponseEntity<>(appResponse, appResponse.getHttpStatus());
	}

	@DeleteMapping
	public ResponseEntity<AppResponse> deleteExchange(@RequestBody Exchange exchange) {
		log.info("Received book exchange delete request");
		bookExchangeService.deleteTransaction(exchange);
		AppResponse appResponse = buildAppResponse("Book exchange deleted successfully", HttpStatus.OK);
		return new ResponseEntity<>(appResponse, appResponse.getHttpStatus());
	}

}
