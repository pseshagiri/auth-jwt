package com.seshagiri.jwt.authapi.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.seshagiri.jwt.authapi.dto.LoginDto;
import com.seshagiri.jwt.authapi.dto.RegistrationDto;
import com.seshagiri.jwt.authapi.entity.User;
import com.seshagiri.jwt.authapi.repo.UserRepo;

@Service()
public class AuthenticationService {
	
	private UserRepo userRepo;
	private AuthenticationManager authenticationManager;
	private PasswordEncoder passwordEncoder;
	
	public AuthenticationService(UserRepo userRepo, AuthenticationManager authenticationManager,
			PasswordEncoder passwordEncoder) {
		
		this.userRepo = userRepo;
		this.authenticationManager = authenticationManager;
		this.passwordEncoder = passwordEncoder;
	}
	
	public User signUp(RegistrationDto dto) {
		
		User user = new User();
		user.setEmail(dto.getEmail());
		user.setFullName(dto.getFullName());
		user.setPassword(passwordEncoder.encode(dto.getPassword()));
		return this.userRepo.save(user);
	}
	
	public User autheticateUser(LoginDto dto) {
		authenticationManager.authenticate(new 
				UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));
		
	  return userRepo.findByEmail(dto.getEmail())
			  .orElseThrow();
	}

}
