package com.rpi.alexandria.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordObj {

  private String email;

  private String resetToken;

  private String newPassword;

}
