package com.rpi.alexandria.controller;

import com.rpi.alexandria.controller.response.AppResponse;
import com.rpi.alexandria.controller.response.JWTResponse;
import com.rpi.alexandria.exception.UserException;
import com.rpi.alexandria.model.User;
import com.rpi.alexandria.service.UserService;
import com.rpi.alexandria.service.security.JwtService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.Base64;

@RestController
@RequestMapping("/api/")
@Slf4j
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ApplicationController {

	UserService userService;

	JwtService jwtService;

	private final AuthenticationManager authenticationManager;

	@GetMapping("/login")
	public ResponseEntity<JWTResponse> login(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
		log.info("Received login request..");
		if (!StringUtils.isEmpty(authHeader)) {
			log.info(authHeader);
			String[] tokens = authHeader.split(" ");
			if (StringUtils.equals(tokens[0], "Basic")) {

				String decodedAuthHeader = new String(Base64.getDecoder().decode(tokens[1]));
				log.info("Decoded auth header: {}", decodedAuthHeader);
				String[] credentials = decodedAuthHeader.split(":");
				String username = credentials[0];
				String password = credentials[1];
				if (userService.isValidUser(username, password)) {
					User user = userService.getUser(username);
					String jwt = jwtService.getJwt(user);
					return ResponseEntity.ok(new JWTResponse(jwt, user.getFirstName(), user.getLastName()));
				}
			}
			if (StringUtils.equals(tokens[0], "Bearer")) {
				String jwt = new String(Base64.getDecoder().decode(tokens[1]));
				if (jwtService.validate(jwt)) {
					return ResponseEntity.ok().build();
				}
			}

		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	@PostMapping("/signup")
	public ResponseEntity<AppResponse> signup(@RequestBody User user) throws UserException {
		log.info("Received request..");
		userService.createUser(user);
		AppResponse appResponse = AppResponse.builder().dateTime(OffsetDateTime.now()).httpStatus(HttpStatus.OK)
				.message("User created successfully").build();
		return new ResponseEntity<>(appResponse, appResponse.getHttpStatus());
	}

	@GetMapping("/home")
	public ResponseEntity<String> home() {
		return ResponseEntity.ok("Home page");
	}

}
