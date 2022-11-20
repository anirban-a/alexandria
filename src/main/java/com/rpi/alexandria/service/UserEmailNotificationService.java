package com.rpi.alexandria.service;

import com.rpi.alexandria.model.Email;
import com.rpi.alexandria.model.EmailValidationCode;
import com.rpi.alexandria.model.IEmailNotification;
import com.rpi.alexandria.model.User;
import com.rpi.alexandria.repository.EmailValidationCodeRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@Slf4j
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserEmailNotificationService {
    INotificationService<IEmailNotification> emailNotificationService;
    EmailValidationCodeRepository validationCodeRepository;

    BCryptPasswordEncoder passwordEncoder;

    public void sendAccountVerificationEmail(User user) {
        String randomText = String.valueOf(OffsetDateTime.now().toInstant().toEpochMilli());

        String code = passwordEncoder.encode(randomText);
        EmailValidationCode validationCode = new EmailValidationCode();
        validationCode.setUserId(user.getUsername());
        validationCode.setValidationCode(code);

        validationCodeRepository.save(validationCode);

        String subject = "Alexandria - Account verification code";
        String message = String.format("Your account validation code is: %s", code);

        Email email = new Email();
        email.setMessage(message);
        email.setSubject(subject);
        email.setRecipient(user.getUsername());

        emailNotificationService.sendNotification(email);
    }
}
