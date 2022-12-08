package com.rpi.alexandria.controller.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@Data
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JWTResponse {

  String jwt;

  String firstName;

  String lastName;

  String email;

}
