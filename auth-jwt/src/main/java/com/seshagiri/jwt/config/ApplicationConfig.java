package com.seshagiri.jwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configurers.userdetails.DaoAuthenticationConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.seshagiri.jwt.authapi.repo.UserRepo;

@Configuration()
public class ApplicationConfig {

	private UserRepo userRepo;

	public ApplicationConfig(UserRepo userRepo) {		
		this.userRepo = userRepo;
	}
	
	@Bean()
	UserDetailsService userDetailService() {
		return username -> userRepo.findByEmail(username)
			.orElseThrow(() -> new UsernameNotFoundException("User Name Not Found"));
	}
	
	@Bean()
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean()
	public AuthenticationManager authenticationManager
	    (AuthenticationConfiguration  config) throws Exception {
		return config.getAuthenticationManager();
	}
	
	@Bean()
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new 
				DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailService());
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}
	
}
