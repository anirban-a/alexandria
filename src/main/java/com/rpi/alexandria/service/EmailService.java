package com.rpi.alexandria.service;

import com.rpi.alexandria.exception.ApplicationException;
import com.rpi.alexandria.model.Email;
import com.rpi.alexandria.model.IEmailNotification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
@Slf4j
public class EmailService implements INotificationService<IEmailNotification> {

    private final String password;
    private final String sender;

    public EmailService(@Value("${application.email.password}") String password, @Value("${application.email.accountId}") String sender) {
        this.password = password;
        this.sender = sender;
    }

    public String sendSimpleEmail(Email details) {
        sendNotification(details);
        return "Mail Sent Successfully...";
    }

    @Override
    public void sendNotification(IEmailNotification notification) {
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
            helper.setTo(notification.getRecipient());
            helper.setSubject(notification.getSubject());
            helper.setText(notification.getMessage());

            // Sending the mail
            Transport.send(message, sender, password);

        }

        // Catch block to handle the exceptions
        catch (Exception e) {
            throw new ApplicationException(e.getMessage());
        }
    }
}
