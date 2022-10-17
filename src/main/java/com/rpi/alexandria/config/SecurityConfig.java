package com.rpi.alexandria.config;

import com.rpi.alexandria.model.User;
import com.rpi.alexandria.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.List;

@EnableWebSecurity
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserRepository userRepository;

    @Autowired
    public SecurityConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

    }

    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(this::getUserDetails);
    }

    private UserDetails getUserDetails(String username) {
        User user = userRepository.findById(username).orElseThrow(
                () -> new UsernameNotFoundException(String.format("User: %s not found", username)));
        return toUserDetails(user);
    }

    private UserDetails toUserDetails(User user) {
        return new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                GrantedAuthority userAuthority = new GrantedAuthority() {
                    @Override
                    public String getAuthority() {
                        return "user";
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
