package com.rpi.alexandria.model;

import lombok.Data;

@Data
public class Rating {
    String usernameOfUserToSendRating;

    String usernameOfUserToRate;

    int rating;
}
