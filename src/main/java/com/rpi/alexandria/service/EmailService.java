package com.rpi.alexandria.service;
package com.SpringBootEmail.service;


import com.SpringBootEmail.Entity.EmailDetails;

public interface EmailService {
    String sendSimpleMail(EmailDetails details);
}
