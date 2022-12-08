package com.rpi.alexandria.model;

import lombok.Data;

@Data
public class Rating {

  // username of account to add rating to, get rating information from, update rating
  // of, or delete rating from
  private String usernameOther;

  private int ratingValue;

}
