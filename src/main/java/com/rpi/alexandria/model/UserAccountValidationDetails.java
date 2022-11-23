package com.rpi.alexandria.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAccountValidationDetails {

	private String email;

	private String validationCode;

}
