package com.rpi.alexandria.controller.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

@AllArgsConstructor
@Builder
@Data
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppResponse {

	String message;

	OffsetDateTime dateTime;

	HttpStatus httpStatus;

	String description;

}
