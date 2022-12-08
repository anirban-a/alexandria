package com.rpi.alexandria.controller;

import com.rpi.alexandria.model.Email;
import com.rpi.alexandria.service.EmailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email/")
@AllArgsConstructor
@Slf4j
public class EmailController {

  @Autowired
  private EmailService emailService;

  @GetMapping("/test")
  public ResponseEntity<String> test() {
    log.info("Test request received...");
    return ResponseEntity.ok("Test page");
  }

  @GetMapping("/sendMail")
  public ResponseEntity<String> sendMail(@RequestBody Email details) {
    log.info("Received email request...");
    String status = emailService.sendSimpleEmail(details);
    log.info(String.format("Status: %s", status));

    return ResponseEntity.ok(String.format("Status: %s", status));
  }

}
