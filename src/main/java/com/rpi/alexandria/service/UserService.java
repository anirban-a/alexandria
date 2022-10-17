package com.rpi.alexandria.service;

import com.rpi.alexandria.model.User;
import com.rpi.alexandria.repository.UserRepository;
import com.rpi.alexandria.service.security.UserDetailsService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserService implements UserDetailsService {
    public static final String AUTHORITY_USER = "user";
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    public void createUser(final User user) {
        String password = user.getPassword();
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        log.info("User saved into DB");
    }

    public boolean isValidUser(final User user) {
        UserDetails userDetails = loadUserByUsername(user.getUsername());
        return passwordEncoder.matches(user.getPassword(), userDetails.getPassword());
    }

    public boolean isValidUser(final String username, String password) {
        UserDetails userDetails = loadUserByUsername(username);
        return passwordEncoder.matches(password, userDetails.getPassword());
    }

    public User getUser(String username){
        return userRepository.findById(username).orElseThrow(
                () -> new UsernameNotFoundException(String.format("User: %s not found", username)));
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
                return true;
            }

            @Override
            public boolean isAccountNonLocked() {
                return false;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return false;
            }

            @Override
            public boolean isEnabled() {
                return true;
            }
        };
    }
}
