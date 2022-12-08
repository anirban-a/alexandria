package com.rpi.alexandria.controller;

import com.rpi.alexandria.controller.response.AppResponse;
import com.rpi.alexandria.model.Donation;
import com.rpi.alexandria.service.BookDonationService;
import com.rpi.alexandria.service.ITransactableBookService;
import com.rpi.alexandria.service.UserService;
import java.util.List;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/book/donation")
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@CrossOrigin("http://localhost:${ui.port}")
public class BookDonationController extends BaseController {

  ITransactableBookService<Donation> bookDonationService;

  public BookDonationController(UserService userService, BookDonationService bookDonationService) {
    super(userService);
    this.bookDonationService = bookDonationService;
  }

  @GetMapping
  public ResponseEntity<AppResponse<List<Donation>>> getAllDonationRequests() {
    String username = getUser().getUsername();
    List<Donation> bookDonationList = bookDonationService.getAllTransactionsByUserId(username);
    AppResponse<List<Donation>> appResponse = buildAppResponse("", HttpStatus.OK);
    appResponse.setData(bookDonationList);
    return new ResponseEntity<>(appResponse, appResponse.getHttpStatus());
  }

  @PostMapping
  public ResponseEntity<AppResponse<Void>> createDonationRequest(@RequestBody Donation donation) {
    log.info("Received book donation request");
    bookDonationService.createTransaction(donation);
    AppResponse<Void> appResponse = buildAppResponse(
        "Request for book donation created successfully",
        HttpStatus.OK);
    return new ResponseEntity<>(appResponse, appResponse.getHttpStatus());
  }

  @PostMapping("/complete/{id}")
  public ResponseEntity<AppResponse<Void>> markComplete(@PathVariable String id) {
    log.info("Received book exchange request");
    String username = getUser().getUsername();
    bookDonationService.markCompleted(id, username);
    AppResponse<Void> appResponse = buildAppResponse("Book donation marked complete successfully",
        HttpStatus.OK);
    return new ResponseEntity<>(appResponse, appResponse.getHttpStatus());
  }

  @DeleteMapping
  public ResponseEntity<AppResponse<Void>> deleteDonation(@RequestBody Donation donation) {
    log.info("Received book exchange delete request");
    bookDonationService.deleteTransaction(donation);
    AppResponse<Void> appResponse = buildAppResponse("Book donation deleted successfully",
        HttpStatus.OK);
    return new ResponseEntity<>(appResponse, appResponse.getHttpStatus());
  }

}
