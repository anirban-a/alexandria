package com.rpi.alexandria.controller;

import com.rpi.alexandria.controller.response.AppResponse;
import com.rpi.alexandria.model.User;
import com.rpi.alexandria.service.UserService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.OffsetDateTime;

@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PROTECTED)
@Slf4j
public class BaseController {

	UserService userService;

	protected User getUser() {
		log.info("Fetching username from Spring security context");
		String username = (String) SecurityContextHolder.getContext().getAuthentication().getName();
		log.info("Username fetched successfully");
		return userService.getUser(username);
	}

	protected AppResponse buildAppResponse(String message, HttpStatus httpStatus) {
		return AppResponse.builder().dateTime(OffsetDateTime.now()).httpStatus(httpStatus).message(message).build();
	}

}
