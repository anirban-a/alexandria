package com.rpi.alexandria.controller;

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
    public ResponseEntity<String> login(@RequestHeader(HttpHeaders.AUTHORIZATION) String authHeader) {
        log.info("Received login request..");
        if (!StringUtils.isEmpty(authHeader)) {
            log.info(authHeader);
			String[]tokens = authHeader.split(" ");
			if(StringUtils.equals(tokens[0], "Basic")){

				String decodedAuthHeader = new String(Base64.getDecoder().decode(tokens[1]));
                log.info("Decoded auth header: {}", decodedAuthHeader);
				String[] credentials = decodedAuthHeader.split(":");
				String username = credentials[0];
				String password = credentials[1];
                if(!userService.isValidUser(username, password)){
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                }
                String jwt = jwtService.getJwt(userService.getUser(username));
                return ResponseEntity.ok(jwt);
			}
			if(StringUtils.equals(tokens[0], "Bearer")){

			}


        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody User user) {
        log.info("Received request..");
        userService.createUser(user);
        return ResponseEntity.ok().build();
    }
}
