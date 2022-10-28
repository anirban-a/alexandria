package com.rpi.alexandria.service;

import com.rpi.alexandria.model.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
@Slf4j
public class EmailService {

	private String sender = "noreply.alexandriaemail@gmail.com";

	public String sendSimpleEmail(Email details) {
		// Try block to check for exceptions
		try {
			Properties prop = new Properties();
			prop.setProperty("mail.host", "smtp.gmail.com");
			prop.setProperty("mail.transport.protocol", "smtp");
			prop.setProperty("mail.smtp.auth", "true");
			prop.setProperty("mail.smtp.starttls.enable", "true");
			prop.setProperty("mail.debug", "true");
			prop.setProperty("mail.smtp.ssl.enable", "true");
			prop.setProperty("mail.test-connection", "true");

			Session session = Session.getInstance(prop);
			MimeMessage message = new MimeMessage(session);

			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(sender);
			helper.setTo(details.getRecipient());
			helper.setSubject(details.getSubject());
			helper.setText(details.getMsgBody());

			// Sending the mail
			Transport.send(message, sender, "password");
			return "Mail Sent Successfully...";
		}

		// Catch block to handle the exceptions
		catch (Exception e) {
			return e.getMessage();
			// return "Error while Sending Mail";
		}
	}

}
