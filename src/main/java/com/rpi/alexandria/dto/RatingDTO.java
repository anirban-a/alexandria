package com.rpi.alexandria.dto;

import lombok.Data;

@Data
public class RatingDTO {

	String usernameOther;

	int ratingValue;

	public RatingDTO(String usernameOther, int ratingValue) {
		this.usernameOther = usernameOther;
		this.ratingValue = ratingValue;
	}

}
