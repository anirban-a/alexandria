package com.rpi.alexandria.service;


import com.rpi.alexandria.model.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
@Slf4j
public class EmailService{

    @Autowired private JavaMailSender mailSender;

    //@Value("${username:noreply.alexandriaemail@gmail.com}") private String sender;
    private String sender = "noreply.alexandriaemail@gmail.com";

    public String sendSimpleEmail(Email details)
    {
        // Try block to check for exceptions
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper;
            helper = new MimeMessageHelper(message, true);
            helper.setFrom(sender);
            helper.setTo(details.getRecipient());
            helper.setSubject(details.getSubject());
            helper.setText(details.getMsgBody());
            log.info("Test1");

            // Creating a simple mail message
            SimpleMailMessage mailMessage
                    = new SimpleMailMessage();
            log.info("Test2");
            // Setting up necessary details
            log.info(sender);
            log.info(details.getRecipient());
            log.info(details.getSubject());
            log.info(details.getMsgBody());

            mailMessage.setFrom(sender);
            mailMessage.setTo(details.getRecipient());
            mailMessage.setText(details.getMsgBody());
            mailMessage.setSubject(details.getSubject());
            log.info("Test3");

            // Sending the mail
            //mailSender.send(mailMessage);
            mailSender.send(message);
            return "Mail Sent Successfully...";
        }

        // Catch block to handle the exceptions
        catch (Exception e) {
            return "Error while Sending Mail";
        }
    }
}
