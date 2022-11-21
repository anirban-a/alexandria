package com.rpi.alexandria.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Email implements IEmailNotification {

	private String recipient;

	private String message;

	private String subject;

}
