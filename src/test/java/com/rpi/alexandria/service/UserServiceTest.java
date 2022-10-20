package com.rpi.alexandria.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rpi.alexandria.model.User;
import com.rpi.alexandria.repository.UserRepository;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceTest {

	@MockBean
	UserRepository userRepository = Mockito.mock(UserRepository.class);

	ObjectMapper objectMapper = new ObjectMapper();

	@Mock
	UserService userService;

	@BeforeAll
	void setup() {
		userService = new UserService(userRepository, new BCryptPasswordEncoder());
	}

	@Test
	void testContextLoads() throws JSONException, IOException {

		User user = objectMapper.readValue(getJson("json/user-001.json"), User.class);
		assertEquals("johhny125", user.getUsername());
	}

	private String getJson(String filePath) throws IOException, JSONException {
		Resource resource = new ClassPathResource(filePath);
		InputStream is = new FileInputStream(resource.getFile());
		String jsonTxt = IOUtils.toString(is, StandardCharsets.UTF_8);
		JSONObject json = new JSONObject(jsonTxt);
		return json.toString();
	}

}
