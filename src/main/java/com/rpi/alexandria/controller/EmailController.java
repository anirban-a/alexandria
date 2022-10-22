package com.rpi.alexandria.controller;


import com.rpi.alexandria.service.email.EmailDetails;
import com.rpi.alexandria.service.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

    @Autowired
    private EmailService emailService;


    @PostMapping("/sendMail")
    public ResponseEntity<String> sendMail(@RequestBody EmailDetails details)
    {
        String status
                = emailService.sendSimpleEmail(details);

        return ResponseEntity.ok(String.format("Status: %s", status));
    }


}
