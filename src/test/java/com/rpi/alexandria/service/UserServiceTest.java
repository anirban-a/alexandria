package com.rpi.alexandria.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rpi.alexandria.model.User;
import com.rpi.alexandria.repository.UniversityRepository;
import com.rpi.alexandria.repository.UserRepository;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceTest {

	// @MockBean
	UserRepository userRepository = Mockito.mock(UserRepository.class);

	UniversityRepository universityRepository = Mockito.mock(UniversityRepository.class);

	ObjectMapper objectMapper = new ObjectMapper();

	@Mock
	UserService userService;

	@BeforeAll
	void setup() {
		userService = new UserService(userRepository, universityRepository, new BCryptPasswordEncoder());
	}

	@Test
	void testUserLoadByUsername() throws JSONException, IOException {

		User user = objectMapper.readValue(getJson("json/user-001.json"), User.class);
		when(userRepository.findById(anyString(), any())).thenReturn(Optional.of(user));

		UserDetails actualUserDetails = userService.loadUserByUsername("abc");

		assertEquals(user.getUsername(), actualUserDetails.getUsername());
		assertFalse(actualUserDetails.isEnabled());
		assertNotNull(user.getUniversity());
		assertEquals("Rensselaer Polytechnic Institute", user.getUniversity().getName());
	}

	private String getJson(String filePath) throws IOException, JSONException {
		Resource resource = new ClassPathResource(filePath);
		InputStream is = new FileInputStream(resource.getFile());
		String jsonTxt = IOUtils.toString(is, StandardCharsets.UTF_8);
		JSONObject json = new JSONObject(jsonTxt);
		return json.toString();
	}

}
