package com.rpi.alexandria.config;

import com.rpi.alexandria.repository.UserRepository;
import com.rpi.alexandria.service.UserService;
import com.rpi.alexandria.service.security.JwtTokenFilter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.http.HttpServletResponse;

@EnableWebSecurity
@AllArgsConstructor
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final UserRepository userRepository;

	private final JwtTokenFilter jwtTokenFilter;

	private final UserService userService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http = http.csrf().disable();

		http = http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and();

		// Set unauthorized requests exception handler
		http = http.exceptionHandling().authenticationEntryPoint((request, response, ex) -> {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
		}).and();

		http.authorizeHttpRequests().antMatchers("/api/login").permitAll().antMatchers("/api/signup").permitAll()
				.antMatchers("/email/sendMail").permitAll().antMatchers("/email/test").permitAll()
				.anyRequest().authenticated();

		// Add JWT token filter
		http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

	}

	// Used by spring security if CORS is enabled.
	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.addAllowedOrigin("*");
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		source.registerCorsConfiguration("/", config);
		return new CorsFilter(source);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder.userDetailsService(userService::loadUserByUsername);
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

}
