package com.rpi.alexandria.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {

	/**
	 * @param name
	 * @return ResponseEntity
	 * @apiNote This is a demo controller.
	 */
	@GetMapping("/{name}")
	public ResponseEntity<String> greetingUser(@PathVariable String name) {
		log.info("Received request..");
		return ResponseEntity.ok(String.format("Hello %s", name));
	}

}
