package com.rpi.alexandria.controller;

import com.rpi.alexandria.controller.response.AppResponse;
import com.rpi.alexandria.dto.BookDTO;
import com.rpi.alexandria.dto.ExchangeDTO;
import com.rpi.alexandria.model.Book;
import com.rpi.alexandria.model.Exchange;
import com.rpi.alexandria.service.BookExchangeService;
import com.rpi.alexandria.service.BookService;
import com.rpi.alexandria.service.ITransactableBookService;
import com.rpi.alexandria.service.UserService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/book/exchange")
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@CrossOrigin("http://localhost:${ui.port}")
public class BookExchangeController extends BaseController {

	ITransactableBookService<Exchange> bookExchangeService;
	BookService bookService;

	public BookExchangeController(UserService userService, BookExchangeService bookExchangeService, BookService bookService) {
		super(userService);
		this.bookExchangeService = bookExchangeService;
		this.bookService = bookService;
	}

	@GetMapping
	public ResponseEntity<AppResponse<List<ExchangeDTO>>> getAllExchange() {
		String username = getUser().getUsername();
		List<Exchange> exchangeList = bookExchangeService.getAllTransactionsByUserId(username);
		List<ExchangeDTO> exchangeDTOList = exchangeList.stream().map(ExchangeDTO::of).collect(Collectors.toList());
		exchangeDTOList
				.forEach(exchangeDTO ->{
					if(Objects.nonNull(exchangeDTO.getFirstPartyBookId())){
						Book firstPartyBook = bookService.findById(exchangeDTO.getFirstPartyBookId());
						exchangeDTO.setFirstPartyBookDetails(BookDTO.of(firstPartyBook));
					}
					if(Objects.nonNull(exchangeDTO.getOtherPartyBookId())){
						Book otherPartyBook = bookService.findById(exchangeDTO.getOtherPartyBookId());
						exchangeDTO.setOtherPartyBookDetails(BookDTO.of(otherPartyBook));
					}
				});
		AppResponse<List<ExchangeDTO>> appResponse = buildAppResponse("", HttpStatus.OK);
		appResponse.setData(exchangeDTOList);
		return new ResponseEntity<>(appResponse, appResponse.getHttpStatus());
	}

	@PostMapping
	public ResponseEntity<AppResponse<Void>> createExchange(@RequestBody Exchange exchange) {
		log.info("Received book exchange request");
		bookExchangeService.createTransaction(exchange);
		AppResponse<Void> appResponse = buildAppResponse("Book marked for exchange successfully", HttpStatus.OK);
		return new ResponseEntity<>(appResponse, appResponse.getHttpStatus());
	}

	@PostMapping("/complete/{id}")
	public ResponseEntity<AppResponse<Void>> markComplete(@PathVariable String id) {
		log.info("Received book exchange request");
		String username = getUser().getUsername();
		bookExchangeService.markCompleted(id, username);
		AppResponse<Void> appResponse = buildAppResponse("Book exchange marked complete successfully", HttpStatus.OK);
		return new ResponseEntity<>(appResponse, appResponse.getHttpStatus());
	}

	// @DeleteMapping("/")
	// public ResponseEntity<AppResponse<Void>> deleteExchange(@PathVariable Exchange
	// exchange) {
	// log.info("Received book exchange delete request");
	// bookExchangeService.deleteTransaction(exchange);
	// AppResponse<Void> appResponse = buildAppResponse("Book exchange deleted
	// successfully", HttpStatus.OK);
	// return new ResponseEntity<>(appResponse, appResponse.getHttpStatus());
	// }

}
