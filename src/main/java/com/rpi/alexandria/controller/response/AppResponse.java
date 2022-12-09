package com.rpi.alexandria.controller.response;

import java.time.OffsetDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Builder
@Data
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppResponse<T> {

  String message;

  OffsetDateTime dateTime;

  HttpStatus httpStatus;

  String description;

  T data;

}
