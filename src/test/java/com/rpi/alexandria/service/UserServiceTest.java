package com.rpi.alexandria.service;

import com.rpi.alexandria.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceTest {

	@MockBean
	UserRepository userRepository = Mockito.mock(UserRepository.class);

	@Mock
	UserService userService;

	@BeforeAll
	void setup() {
		userService = new UserService(userRepository, new BCryptPasswordEncoder());
	}

	@Test
	void testContextLoads() {
		assertTrue(true);
	}

}
