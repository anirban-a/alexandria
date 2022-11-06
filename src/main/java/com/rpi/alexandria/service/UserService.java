package com.rpi.alexandria.service;

import com.azure.cosmos.models.PartitionKey;
import com.rpi.alexandria.exception.UserException;
import com.rpi.alexandria.model.University;
import com.rpi.alexandria.model.User;
import com.rpi.alexandria.repository.UniversityRepository;
import com.rpi.alexandria.repository.UserRepository;
import com.rpi.alexandria.service.security.UserDetailsService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Slf4j
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserService implements UserDetailsService {

	public static final String AUTHORITY_USER = "user";

	UserRepository userRepository;

	UniversityRepository universityRepository;

	PasswordEncoder passwordEncoder;

	public void createUser(final User user) throws UserException {
		if (userRepository.findById(user.getUsername(), new PartitionKey(user.getUsername())).isPresent()) {
			throw new UserException(String.format("A user by the username %s already exists", user.getUsername()));
		}
		if (ObjectUtils.isEmpty(user.getUniversity()) || StringUtils.isEmpty(user.getUniversity().getName())) {
			throw new UserException("An invalid university name provided");
		}
		String universityId = user.getUniversity().computeId();
		Optional<University> universityOptional = universityRepository.findById(universityId);
		if (universityOptional.isEmpty()) {
			throw new UserException(
					String.format("University name %s provided not found", user.getUniversity().getName()));
		}
		String password = user.getPassword();
		user.setIsAccountActive(true);
		user.setIsVerified(false);
		// user.setVerified(false); // by-default it should be false. It should be true if
		// the email-id has been verified.
		user.setPassword(passwordEncoder.encode(password));
		user.setUniversity(universityOptional.get());
		userRepository.save(user);
		log.info("User saved into DB");
	}

	// generate random n-char long alphanumeric(all caps) string
	public String generateRandomString(int n) {
		Random random = new Random();
		String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		StringBuilder randomStr = new StringBuilder("");
		for (int i = 0; i < n; i++) {
			randomStr.append(alpha.charAt(random.nextInt(alpha.length())));
		}
		return randomStr.toString();
	}

	public String generateResetToken(String emailAddress) {
		String resetToken = generateRandomString(6);
		User user = getUser(emailAddress);
		user.setPasswordResetToken(passwordEncoder.encode(resetToken));
		userRepository.save(user);
		return resetToken;
	}

	public boolean compareResetToken(String emailAddress, String resetToken) {
		User user = getUser(emailAddress);
		String encodedResetToken = passwordEncoder.encode(resetToken);
		String encodedSavedToken = user.getPasswordResetToken();
		return encodedSavedToken.equals(encodedResetToken);
	}

	public boolean updatePassword(String email, String token, String newPassword) {
		if (compareResetToken(email, token)) {
			User user = getUser(email);
			user.setPassword(passwordEncoder.encode(newPassword));
			user.setPasswordResetToken("");
			userRepository.save(user);
			return true;
		}
		return false;
	}

	public boolean isValidUser(final User user) {
		UserDetails userDetails = loadUserByUsername(user.getUsername());
		return passwordEncoder.matches(user.getPassword(), userDetails.getPassword()) && userDetails.isEnabled();
	}

	public boolean isValidUser(final String username, String password) {
		UserDetails userDetails = loadUserByUsername(username);
		return passwordEncoder.matches(password, userDetails.getPassword()) && userDetails.isEnabled();
	}

	public User getUser(String username) {
		return userRepository.findById(username, new PartitionKey(username))
				.orElseThrow(() -> new UsernameNotFoundException(String.format("User: %s not found", username)));
	}

	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		final User user = getUser(username);
		return toUserDetails(user);
	}

	private UserDetails toUserDetails(User user) {
		return new UserDetails() {
			@Override
			public Collection<? extends GrantedAuthority> getAuthorities() {
				GrantedAuthority userAuthority = new GrantedAuthority() {
					@Override
					public String getAuthority() {
						return AUTHORITY_USER;
					}
				};
				return List.of(userAuthority);
			}

			@Override
			public String getPassword() {
				return user.getPassword();
			}

			@Override
			public String getUsername() {
				return user.getUsername();
			}

			@Override
			public boolean isAccountNonExpired() {
				return user.getIsAccountActive();
			}

			@Override
			public boolean isAccountNonLocked() {
				return user.getIsVerified();
			}

			@Override
			public boolean isCredentialsNonExpired() {
				return isAccountNonExpired();
			}

			@Override
			public boolean isEnabled() {
				return isAccountNonExpired() && isAccountNonLocked();
			}
		};
	}

}
